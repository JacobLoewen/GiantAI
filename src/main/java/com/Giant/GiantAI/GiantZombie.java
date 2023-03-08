package com.Giant.GiantAI;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Random;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GiantZombie extends EntityGiantZombie {
    private ScheduledExecutorService executor;
    private ScheduledExecutorService executorInit;
    private static int initialDelay = 2000;
    private static int period = 5000;
    public static final EntityPlayer player = Minecraft.getMinecraft().player;
    static Timer timer;
    public GiantZombie(World world) {
        super(world);
        try {
            this.setSize(1.95F, 6.0F);
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(this::doUpdate, 2500, 5000, TimeUnit.MILLISECONDS);
            executorInit = Executors.newSingleThreadScheduledExecutor();
            executorInit.scheduleAtFixedRate(this::doUpdateInit, 1000, 1000, TimeUnit.MILLISECONDS);

        }
        catch (Exception e) {
            String message = e.getMessage();
            player.sendMessage(new TextComponentString(message + "A"));
        }
    }


    public void doUpdate() {
        //        super.onUpdate();
        try {
            EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 300);
//                nearestPlayer.sendMessage(new TextComponentString("Iteration"));
            if (!nearestPlayer.isDead) {
                this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);
                if (nearestPlayer != null) {
                    double distanceToPlayer = this.getDistanceSq(nearestPlayer);

                    if(distanceToPlayer >= 400) {
                        this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);

                        Vec3d vectorToPlayer = nearestPlayer.getPositionVector().subtract(this.getPositionVector()).normalize();
                        try {
                            BlockPos targetPos = new BlockPos(this.getPositionVector().add(vectorToPlayer.scale(18)));
                            if (world.isAreaLoaded(new BlockPos(targetPos.getX(), targetPos.getY(), targetPos.getZ()), 1)) {
                                this.getNavigator().tryMoveToXYZ(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
                            }
                            else{
                                nearestPlayer.sendMessage(new TextComponentString("Not Loaded!" + "R"));

                            }
                        } catch (Exception e) {
                            String message = e.getMessage();
                            nearestPlayer.sendMessage(new TextComponentString(message + "R"));
                            nearestPlayer.sendMessage(new TextComponentString("Error Message Above"));

                        }
                    }
                }


            }
        }
        catch (Exception e) {
            String message = e.getMessage();
            player.sendMessage(new TextComponentString(message + "B"));
        }
        //        destroyCount = 0;
        //        if (this.destroyTick > 0) {
        //            this.destroyTick--;
        //        } else {
        //            this.destroyTick = 40; // 2 seconds
        //            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Base Value Test: " + this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue()));
        //
        //
        //            int range = 5;
        //            int yRange = 20;
        //            int x, y, z;
        //            Random rx = new Random();
        //            int randX = rx.nextInt(10) + 1 - range;
        //            for (x = -range; x <= range; x++) {
        //                int randY = rx.nextInt(40) + 1 - yRange;
        //                for (y = -yRange; y <= yRange; y++) {
        //                    int randZ = rx.nextInt(10) + 1 - range;
        //                    for (z = -range; z <= range; z++) {
        //                        BlockPos blockPos = new BlockPos(this.posX + randX, this.posY + randY, this.posZ + randZ);
        //                        IBlockState state = this.world.getBlockState(blockPos);
        //                        Block block = state.getBlock();
        //
        //                        if (block instanceof BlockLeaves) {
        //                            this.world.destroyBlock(blockPos, false);
        //                            this.destroyCount++;
        //                        }
        //
        //                        if (this.destroyCount >= 20) {
        //                            return;
        //                        }
        //                    }
        //                }
        //            }
        //        }
    }

    public void doUpdateInit() {
        try {
            EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 300);

            assert nearestPlayer != null;
            double distanceToPlayer = this.getDistanceSq(nearestPlayer);
            if (distanceToPlayer < 400) { // 20 blocks squared
                this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);

                this.getNavigator().tryMoveToEntityLiving(nearestPlayer, 1.0);
                this.attackEntity(nearestPlayer);
            }
        }
        catch(Exception e) {
            String message = e.getMessage();
            player.sendMessage(new TextComponentString(message + "H"));
        }
    }

    int c1 = 0;
    public void attackEntity(EntityPlayer player) {
        try {
            if (this.getDistance(player) < 4F) {
                player.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
            }
        }
        catch (Exception e) {
            String message = e.getMessage();
            player.sendMessage(new TextComponentString(message + "C"));
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(19.0D);
    }

    @Override
    public String getName() {
        return "Giant";
    }


    @Override
    public boolean isChild() {
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

//        @Override
//        public boolean getCanSpawnHere() {
//            return super.getCanSpawnHere() && this.world.checkNoEntityCollision(this.getEntityBoundingBox())
//                    && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
//                    && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
//        }

//        @Override
//        public boolean canBePushed() {
//            return false;
//        }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    //        @Override
//        public void onLivingUpdate() {
//            try {
//                super.onLivingUpdate();
//                this.setAttackTarget(this.world.getNearestAttackablePlayer(this, 34.0D, 34.0D));
//
//            }
//            catch (Exception e) {
//                String message = e.getMessage();
//                player.sendMessage(new TextComponentString(message));
//            }
//        }
    @Override
    public void onRemovedFromWorld() {
        try {
            super.onRemovedFromWorld();
            executorInit.shutdownNow(); // Stop the scheduled task when the entity is removed
            executor.shutdownNow(); // Stop the scheduled task when the entity is removed

        }
        catch (Exception e) {
            String message = e.getMessage();
            player.sendMessage(new TextComponentString(message + "D"));
        }
    }
}
