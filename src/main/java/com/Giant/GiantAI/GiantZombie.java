package com.Giant.GiantAI;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GiantZombie extends EntityGiantZombie {
    private ScheduledExecutorService executor;
    private ScheduledExecutorService executorInit;
    public static final EntityPlayer player = Minecraft.getMinecraft().player;
    public GiantZombie(World world) {
        super(world);
        try {
            this.setSize(1.95F, 6.0F);
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(this::doUpdate, 2500, 5000, TimeUnit.MILLISECONDS);
            executorInit = Executors.newSingleThreadScheduledExecutor();
            executorInit.scheduleAtFixedRate(this::doUpdateInit, 1000, 1000, TimeUnit.MILLISECONDS);
            if(player != null){
                player.sendMessage(new TextComponentString("GiantZombie Spawned In!"));
            }
        }
        catch (Exception e) {
            String message = e.getMessage();
            if(player != null) {
                player.sendMessage(new TextComponentString(message + "A"));
            }
            e.printStackTrace();
        }
    }


    public void doUpdate() {
        try {
            EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 1000);
            if (nearestPlayer != null) {
                if (!nearestPlayer.isDead) {
                    double distanceToPlayer = this.getDistanceSq(nearestPlayer);

                    if(distanceToPlayer >= 400) {
                        this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);

                        Vec3d vectorToPlayer = nearestPlayer.getPositionVector().subtract(this.getPositionVector()).normalize();
                        try {
                            BlockPos targetPos = new BlockPos(this.getPositionVector().add(vectorToPlayer.scale(18)));
                            if (world.isAreaLoaded(new BlockPos(targetPos.getX(), targetPos.getY(), targetPos.getZ()), 20)) {
                                this.getNavigator().tryMoveToXYZ(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
                            }
                            else{
                                if(nearestPlayer != null) {
                                    nearestPlayer.sendMessage(new TextComponentString("Not Loaded!" + "R"));
                                }
                            }
                        } catch (Exception e) {
                            String message = e.getMessage();
                            if (nearestPlayer != null) {


                                nearestPlayer.sendMessage(new TextComponentString(message + "R"));
                                nearestPlayer.sendMessage(new TextComponentString("Error Message Above"));
                            }
                            e.printStackTrace();
                        }
                    }
                }


            }
        }
        catch (Exception e) {
            String message = e.getMessage();
            if(player != null) {
                player.sendMessage(new TextComponentString(message + "B"));
            }
            e.printStackTrace();
        }
    }

    public void doUpdateInit() {
        try {
            EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 1000);

            if(nearestPlayer != null) {
                double distanceToPlayer = this.getDistanceSq(nearestPlayer);
                if (distanceToPlayer < 400) { // 20 blocks squared
                    this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);

                    this.getNavigator().tryMoveToEntityLiving(nearestPlayer, 1.0);
                    this.attackEntity(nearestPlayer);
                }
            }
        }
        catch(Exception e) {
            String message = e.getMessage();
            if(player != null) {
                player.sendMessage(new TextComponentString(message + "H"));
            }
            e.printStackTrace();
        }
    }

    public void attackEntity(EntityPlayer player) {
        try {
            if(player != null) {
                if (this.getDistance(player) < 4F) {
                    player.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
                }
            }
        }
        catch (Exception e) {
            String message = e.getMessage();
            if(player != null) {
                player.sendMessage(new TextComponentString(message + "C"));
            }
            e.printStackTrace();
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

    @Override
    public boolean getCanSpawnHere() {
        boolean flag = super.getCanSpawnHere() && this.world.checkNoEntityCollision(this.getEntityBoundingBox())
                && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
                && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
        if(player != null){
            player.sendMessage(new TextComponentString("CanSpawnHere: " + flag));
        }
        return flag;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

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
            if(player != null) {
                player.sendMessage(new TextComponentString("Removed from world"));
            }

        }
        catch (Exception e) {
            String message = e.getMessage();
            if(player != null) {
                player.sendMessage(new TextComponentString(message + "D"));
            }
            e.printStackTrace();
        }
    }
}



//package com.Giant.GiantAI;
//
//import net.minecraft.entity.SharedMonsterAttributes;
//import net.minecraft.entity.monster.EntityGiantZombie;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.TextComponentString;
//import net.minecraft.world.World;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//public class GiantZombie extends EntityGiantZombie {
////    private ScheduledExecutorService executor;
//    public final ScheduledExecutorService executorInit;
//    public final ScheduledExecutorService executorInitTest;
//
//    public GiantZombie(World world) {
//        super(world);
//        this.setSize(1.95F, 6.0F);
//        executorInit = Executors.newSingleThreadScheduledExecutor();
//        executorInit.scheduleAtFixedRate(this::doUpdateInit, 2500, 5000, TimeUnit.MILLISECONDS);
//        executorInitTest = Executors.newSingleThreadScheduledExecutor();
//        executorInitTest.scheduleAtFixedRate(this::doUpdateInitTest, 0, 1000, TimeUnit.MILLISECONDS);
//
//    }
//
//
////    public void doUpdate() {
////        //        super.onUpdate();
////        try {
////            EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 1000);
//////                nearestPlayer.sendMessage(new TextComponentString("Iteration"));
////            if (nearestPlayer != null) {
////                this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);
////                if (!nearestPlayer.isDead) {
////                    double distanceToPlayer = this.getDistanceSq(nearestPlayer);
////
////                    if(distanceToPlayer > 400) {
////                        this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);
////
////                        //Appears to be the problem in most circumstances; play without it
//////                        Vec3d vectorToPlayer = nearestPlayer.getPositionVector().subtract(this.getPositionVector()).normalize();
//////                        try {
//////                            if (vectorToPlayer.lengthSquared() > 0) {
//////                                {
//////                                    BlockPos targetPos = new BlockPos(this.getPositionVector().add(vectorToPlayer.scale(18)));
//////                                    if (targetPos != null && world.isAreaLoaded(new BlockPos(targetPos.getX(), targetPos.getY(), targetPos.getZ()), 20)) {
//////                                        this.getNavigator().tryMoveToXYZ(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
//////                                    } else {
//////                                        nearestPlayer.sendMessage(new TextComponentString("Not Loaded!" + "R"));
//////
//////                                    }
//////                                }
//////                            }
//////                        }
//////                        catch (Exception e) {
//////                            String message = e.getMessage();
//////                            nearestPlayer.sendMessage(new TextComponentString(message + "R"));
//////                            nearestPlayer.sendMessage(new TextComponentString("Error Message Above"));
//////                            e.printStackTrace();
//////
//////                        }
////
//////                        this.getNavigator().tryMoveToXYZ(, 1.0);
////                    }
////                }
////
////
////            }
////        }
////        catch (Exception e) {
////            String message = e.getMessage();
////            e.printStackTrace();
////        }
//        //        destroyCount = 0;
//        //        if (this.destroyTick > 0) {
//        //            this.destroyTick--;
//        //        } else {
//        //            this.destroyTick = 40; // 2 seconds
//        //            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Base Value Test: " + this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue()));
//        //
//        //
//        //            int range = 5;
//        //            int yRange = 20;
//        //            int x, y, z;
//        //            Random rx = new Random();
//        //            int randX = rx.nextInt(10) + 1 - range;
//        //            for (x = -range; x <= range; x++) {
//        //                int randY = rx.nextInt(40) + 1 - yRange;
//        //                for (y = -yRange; y <= yRange; y++) {
//        //                    int randZ = rx.nextInt(10) + 1 - range;
//        //                    for (z = -range; z <= range; z++) {
//        //                        BlockPos blockPos = new BlockPos(this.posX + randX, this.posY + randY, this.posZ + randZ);
//        //                        IBlockState state = this.world.getBlockState(blockPos);
//        //                        Block block = state.getBlock();
//        //
//        //                        if (block instanceof BlockLeaves) {
//        //                            this.world.destroyBlock(blockPos, false);
//        //                            this.destroyCount++;
//        //                        }
//        //
//        //                        if (this.destroyCount >= 20) {
//        //                            return;
//        //                        }
//        //                    }
//        //                }
//        //            }
//        //        }
//
//    public void doUpdateInit() {
//        try {
////
////            EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 1000);
////            if (nearestPlayer != null && !nearestPlayer.isDead) {
////                double distanceToPlayer = this.getDistanceSq(nearestPlayer);
////                if (distanceToPlayer < 400) { // 20 blocks squared
////                    this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);
////                    if(nearestPlayer != null) {
////                        this.getNavigator().tryMoveToXYZ(nearestPlayer.getPosition().getX(), nearestPlayer.getPosition().getY(), nearestPlayer.getPosition().getZ(), 1.0);
////                        this.attackEntity(nearestPlayer);
////                    }
////                }
////            }
////        }
////        catch(Exception e) {
////            String message = e.getMessage();
////            EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 1000);
////
////            nearestPlayer.sendMessage(new TextComponentString(message + "ERROR"));
////            e.printStackTrace();
////        }
//
//            EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 1000);
//            System.out.println("RUNNING INIT");
//            if (nearestPlayer != null) {
//                double distance = this.getDistanceSq(nearestPlayer);
//                nearestPlayer.sendMessage(new TextComponentString("Distance 400: " + distance));
//
//                if (distance >= 400) {
//                    this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);
//
////                BlockPos blockPos = new BlockPos(this.getPosition());
//
//                    double gx = this.getPosition().getX();
//                    double gy = this.getPosition().getY();
//                    double gz = this.getPosition().getZ();
//
//                    double dx = gx - nearestPlayer.getPosition().getX();
//                    double dy = gy - nearestPlayer.getPosition().getY();
//                    double dz = gz - nearestPlayer.getPosition().getZ();
//
//
//                    while (distance > 324) {
//                        gx = gx - (dx / 2);
//                        gy = gy - (dy / 2);
//                        gz = gz - (dz / 2);
//                        distance = this.getDistanceSq(new BlockPos(gx, gy, gz));
////                    nearestPlayer.sendMessage(new TextComponentString("LOOP: BlockPos: " + gx + ", " +  gy + ", " + gz));
////                    nearestPlayer.sendMessage(new TextComponentString("LOOP: Distance: " + distance));
//
//
//                    }
//                    this.getNavigator().tryMoveToXYZ(gx, gy, gz, 1.0);
//                    nearestPlayer.sendMessage(new TextComponentString("NAVIGATING TO: BlockPos: " + gx + ", " + gy + ", " + gz));
//                    nearestPlayer.sendMessage(new TextComponentString("NAGIVATING: Distance: " + distance));
//
//                }
//            }
//        }
//        catch(Exception e) {
//            String message = e.getMessage();
//            e.printStackTrace();
//        }
//    }
//
////    public BlockPos getNextBlockTowardsPlayer(BlockPos currentPos, BlockPos playerPos) {
////        EnumFacing direction = getDirectionTowardsPlayer(currentPos, playerPos);
////        return currentPos.offset(direction);
////    }
////
////    public EnumFacing getDirectionTowardsPlayer(BlockPos currentPos, BlockPos playerPos) {
////        double dX = playerPos.getX() - currentPos.getX() + 0.5;
////        double dY = playerPos.getY() - currentPos.getY() + 0.5;
////        double dZ = playerPos.getZ() - currentPos.getZ() + 0.5;
////
////        if (Math.abs(dX) > Math.abs(dY) && Math.abs(dX) > Math.abs(dZ)) {
////            return (dX > 0) ? EnumFacing.EAST : EnumFacing.WEST;
////        } else if (Math.abs(dY) > Math.abs(dX) && Math.abs(dY) > Math.abs(dZ)) {
////            return (dY > 0) ? EnumFacing.UP : EnumFacing.DOWN;
////        } else {
////            return (dZ > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH;
////        }
////    }
//
//
//
//
//
//    public void doUpdateInitTest(){
//        EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 1000);
//        if(nearestPlayer != null) {
//            double distance = this.getDistanceSq(nearestPlayer);
//            if (distance < 400) {
//                this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);
//                this.getNavigator().tryMoveToEntityLiving(nearestPlayer, 1.0);
//                this.attackEntity(nearestPlayer);
//            }
//        }
//    }
////    public void doUpdateInitTest() {
////        try {
////            EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 1000);
////            if (nearestPlayer!= null &&!nearestPlayer.isDead) {
////                double distanceToPlayer = this.getDistanceSq(nearestPlayer);
////                if (distanceToPlayer < 400) { // 20 blocks squared
////                    this.getLookHelper().setLookPositionWithEntity(nearestPlayer, 10.0F, 10.0F);
////                    if(nearestPlayer!= null) {
////                        this.getNavigator().tryMoveToXYZ(nearestPlayer.getPosition().getX(), nearestPlayer.getPosition().getY(), nearestPlayer.getPosition().getZ(), 1.0);
////                        this.attackEntity(nearestPlayer);
////                    }
////                }
////            }
////        }
////        EntityPlayer nearestPlayer = world.getClosestPlayerToEntity(this, 1000);
////
////        if (nearestPlayer!= null && !nearestPlayer.isDead) {
////
////            this.getLookHelper().setLookPositionWithEntity(this, 10.0F, 10.0F);
//////            this.getNavigator().tryMoveToEntityLiving(nearestPlayer.getPosition().getX(), nearestPlayer.getPosition().getY(), nearestPlayer.getPosition().getZ(), 1.0);
////            this.getNavigator().tryMoveToEntityLiving(nearestPlayer, 1.0);
////
////            this.attackEntity(nearestPlayer);
////        }
////    }
//
//
//    public void attackEntity(EntityPlayer player) {
//        try {
//            if(!player.isDead){
//                if (this.getDistance(player) < 4F) {
//                    player.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
//                }
//            }
//        }
//        catch (Exception e) {
//            String message = e.getMessage();
//            player.sendMessage(new TextComponentString(message + "ATTACK ERROR"));
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void applyEntityAttributes() {
//        super.applyEntityAttributes();
//        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
//        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
//        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(50.0D);
//        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
//    }
//
//    @Override
//    public String getName() {
//        return "Giant";
//    }
//
//
//    @Override
//    public boolean isChild() {
//        return false;
//    }
//
//    @Override
//    protected boolean canDespawn() {
//        return false;
//    }
//
////        @Override
////        public boolean getCanSpawnHere() {
////            return super.getCanSpawnHere() && this.world.checkNoEntityCollision(this.getEntityBoundingBox())
////                    && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
////                    && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
////        }
//
////        @Override
////        public boolean canBePushed() {
////            return false;
////        }
//
//    @Override
//    public boolean canBeCollidedWith() {
//        return true;
//    }
//
//    //        @Override
////        public void onLivingUpdate() {
////            try {
////                super.onLivingUpdate();
////                this.setAttackTarget(this.world.getNearestAttackablePlayer(this, 34.0D, 34.0D));
////
////            }
////            catch (Exception e) {
////                String message = e.getMessage();
////                player.sendMessage(new TextComponentString(message));
////            }
////        }
////    @Override
////    public void onRemovedFromWorld() {
////        try {
////            super.onRemovedFromWorld();
////            if(this.isDead) {
////                executorInitTest.shutdownNow(); // Stop the scheduled task when the entity is removed
////                executorInit.shutdownNow();
////                System.out.println("Entity " + this.getName() + " has been removed from the world");
////            }
////
////        }
////        catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//}
