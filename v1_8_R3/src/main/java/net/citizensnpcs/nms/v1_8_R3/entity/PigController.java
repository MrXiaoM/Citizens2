package net.citizensnpcs.nms.v1_8_R3.entity;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPig;
import org.bukkit.entity.Pig;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.nms.v1_8_R3.util.NMSBoundingBox;
import net.citizensnpcs.nms.v1_8_R3.util.NMSImpl;
import net.citizensnpcs.npc.CitizensNPC;
import net.citizensnpcs.npc.ai.NPCHolder;
import net.citizensnpcs.util.NMS;
import net.citizensnpcs.util.Util;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLightning;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;

public class PigController extends MobEntityController {
    public PigController() {
        super(EntityPigNPC.class);
    }

    @Override
    public Pig getBukkitEntity() {
        return (Pig) super.getBukkitEntity();
    }

    public static class EntityPigNPC extends EntityPig implements NPCHolder {
        @Override
        public boolean a(EntityPlayer player) {
            return NMS.shouldBroadcastToPlayer(npc, () -> super.a(player));
        }

        private final CitizensNPC npc;

        public EntityPigNPC(World world) {
            this(world, null);
        }

        public EntityPigNPC(World world, NPC npc) {
            super(world);
            this.npc = (CitizensNPC) npc;
        }

        @Override
        public void a(AxisAlignedBB bb) {
            super.a(NMSBoundingBox.makeBB(npc, bb));
        }

        @Override
        public void a(boolean flag) {
            if (npc == null) {
                super.a(flag);
                return;
            }
            NMSImpl.checkAndUpdateHeight(this, flag, super::a);
        }

        @Override
        protected void a(double d0, boolean flag, Block block, BlockPosition blockposition) {
            if (npc == null || !npc.isFlyable()) {
                super.a(d0, flag, block, blockposition);
            }
        }

        @Override
        public void a(Entity entity, float strength, double dx, double dz) {
            NMS.callKnockbackEvent(npc, strength, dx, dz, evt -> super.a(entity, (float) evt.getStrength(),
                    evt.getKnockbackVector().getX(), evt.getKnockbackVector().getZ()));
        }

        @Override
        public int aE() {
            return NMS.getFallDistance(npc, super.aE());
        }

        @Override
        public boolean aL() {
            return npc == null ? super.aL() : npc.isPushableByFluids();
        }

        @Override
        public float bE() {
            return NMS.getJumpPower(npc, super.bE());
        }

        @Override
        protected String bo() {
            return NMSImpl.getSoundEffect(npc, super.bo(), NPC.Metadata.HURT_SOUND);
        }

        @Override
        protected String bp() {
            return NMSImpl.getSoundEffect(npc, super.bp(), NPC.Metadata.DEATH_SOUND);
        }

        @Override
        public boolean bW() {
            if (npc == null)
                return super.bW();
            return false;
        }

        @Override
        public boolean cc() {
            return NMSImpl.isLeashed(npc, super::cc, this);
        }

        @Override
        public void collide(net.minecraft.server.v1_8_R3.Entity entity) {
            // this method is called by both the entities involved - cancelling
            // it will not stop the NPC from moving.
            super.collide(entity);
            if (npc != null) {
                Util.callCollisionEvent(npc, entity.getBukkitEntity());
            }
        }

        @Override
        public boolean d(NBTTagCompound save) {
            return npc == null ? super.d(save) : false;
        }

        @Override
        protected void D() {
            if (npc == null) {
                super.D();
            }
        }

        @Override
        public void e(float f, float f1) {
            if (npc == null || !npc.isFlyable()) {
                super.e(f, f1);
            }
        }

        @Override
        public void E() {
            super.E();
            if (npc != null) {
                npc.update();
            }
        }

        @Override
        public void g(double x, double y, double z) {
            Vector vector = Util.callPushEvent(npc, x, y, z);
            if (vector != null) {
                super.g(vector.getX(), vector.getY(), vector.getZ());
            }
        }

        @Override
        public void g(float f, float f1) {
            if (npc == null || !npc.isFlyable()) {
                super.g(f, f1);
            } else {
                NMSImpl.flyingMoveLogic(this, f, f1);
            }
        }

        @Override
        public CraftEntity getBukkitEntity() {
            if (npc != null && !(bukkitEntity instanceof NPCHolder)) {
                bukkitEntity = new PigNPC(this);
            }
            return super.getBukkitEntity();
        }

        @Override
        public NPC getNPC() {
            return npc;
        }

        @Override
        public boolean k_() {
            if (npc == null || !npc.isFlyable())
                return super.k_();
            else
                return false;
        }

        @Override
        public void onLightningStrike(EntityLightning entitylightning) {
            if (npc == null) {
                super.onLightningStrike(entitylightning);
            }
        }

        @Override
        protected String z() {
            return NMSImpl.getSoundEffect(npc, super.z(), NPC.Metadata.AMBIENT_SOUND);
        }
    }

    public static class PigNPC extends CraftPig implements NPCHolder {
        private final CitizensNPC npc;

        public PigNPC(EntityPigNPC entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.npc = entity.npc;
        }

        @Override
        public NPC getNPC() {
            return npc;
        }
    }
}