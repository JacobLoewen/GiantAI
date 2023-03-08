package com.Giant.GiantAI;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Random;
import java.util.Timer;

public class GiantZombie extends EntityGiantZombie {
    static Timer timer;
    public GiantZombie(World world) {
        super(world);
        this.setSize(1.95F, 6.0F);
    }

    public int destroyTick = 40;
    public int destroyCount;

    @Override
    public void onUpdate() {
        super.onUpdate();

        EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 300);
        if (nearestPlayer != null && !nearestPlayer.isDead) {
            this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);
//            this.getNavigator().tryMoveToXYZ(nearestPlayer.getPosition().getX(),nearestPlayer.getPosition().getY(),nearestPlayer.getPosition().getZ(), 1.0);
//            double xp = nearestPlayer.getPosition().getX();
//            double yp = nearestPlayer.getPosition().getY();
//            double zp = nearestPlayer.getPosition().getZ();
//
//            double xg = this.getPosition().getX();
//            double yg = this.getPosition().getY();
//            double zg = this.getPosition().getZ();
//
//            double dx = xg - xp;
//            double dy = yg - yp;
//            double dz = zg - zp;
//
//
//            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
//
//            if(dist > 18){
//                if (xg > xp){
//
//                }
//                this.getNavigator().tryMoveToXYZ(x, y, z, 1.0);
//            }
            EntityPlayer closestPlayer = this.world.getClosestPlayerToEntity(this, 300);
            if (closestPlayer != null) {
                double distanceToPlayer = this.getDistanceSq(closestPlayer);
                if (distanceToPlayer < 400) { // 20 blocks squared

                    this.getNavigator().tryMoveToXYZ(closestPlayer.posX, closestPlayer.posY, closestPlayer.posZ, 1.0);
                }
                else {

                    Vec3d vectorToPlayer = closestPlayer.getPositionVector().subtract(this.getPositionVector()).normalize();
                    BlockPos targetPos = new BlockPos(this.getPositionVector().add(vectorToPlayer.scale(18)));
                    this.getNavigator().tryMoveToXYZ(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
                }
            }

            this.attackEntity(nearestPlayer);


        }
//        int range = 5; // Change this to adjust the range of destruction
//        for (int x = -range; x <= range; x++) {
//            for (int y = -range; y <= range; y++) {
//                for (int z = -range; z <= range; z++) {
//                    BlockPos pos = new BlockPos(this.posX + x, this.posY + y, this.posZ + z);
//                    Block block = this.world.getBlockState(pos).getBlock();
//                    if (block instanceof BlockLeaves || block instanceof BlockDirt) {
//                        // Destroy the block
//                        this.world.destroyBlock(pos, true);
//                    }
//                }
//            }
//        }
        destroyCount = 0;
        if (this.destroyTick > 0) {
            this.destroyTick--;
        } else {
            this.destroyTick = 40; // 2 seconds
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Base Value Test: " + this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue()));


            int range = 5;
            int yRange = 20;
            int x, y, z;
            Random rx = new Random();
            int randX = rx.nextInt(10) + 1 - range;
            for (x = -range; x <= range; x++) {
                int randY = rx.nextInt(40) + 1 - yRange;
                for (y = -yRange; y <= yRange; y++) {
                    int randZ = rx.nextInt(10) + 1 - range;
                    for (z = -range; z <= range; z++) {
                        BlockPos blockPos = new BlockPos(this.posX + randX, this.posY + randY, this.posZ + randZ);
                        IBlockState state = this.world.getBlockState(blockPos);
                        Block block = state.getBlock();

                        if (block instanceof BlockLeaves || block instanceof BlockDirt) {
                            this.world.destroyBlock(blockPos, false);
                            this.destroyCount++;
                        }

                        if (this.destroyCount >= 20) { // destroy only a third of the blocks
                            return;
                        }
                    }
                }
            }
        }
    }


    int c1 = 0;
    public void attackEntity(EntityPlayer player) {
        if (this.getDistance(player) < 4F) {
            timer =new Timer();
            timer.schedule(new DisplayCountdown(), 0, 100);
            player.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(25.0D);
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

    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.world.checkNoEntityCollision(this.getEntityBoundingBox())
                && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
                && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
    }

//    @Override
//    public boolean canSpawnAtDifficulty() {
//        return true;
//    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.setAttackTarget(this.world.getNearestAttackablePlayer(this, 200.0D, 200.0D));
    }




//    public boolean attackEntityAsMob(EntityLivingBase entity) {
//        boolean success = super.attackEntityAsMob(entity);
//        if (success && entity instanceof EntityPlayer) {
//            EntityPlayer player = (EntityPlayer) entity;
//            player.addExperience(50);
//        }
//        return success;
//    }

}