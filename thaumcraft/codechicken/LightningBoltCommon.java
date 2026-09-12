package thaumcraft.codechicken;

import forge.ISpecialResistance;
import java.util.*;
import net.minecraft.src.*;

public class LightningBoltCommon
{
    ArrayList segments;
    WRVector3 start;
    WRVector3 end;
    HashMap splitparents;
    public float multiplier;
    public float length;
    public int numsegments0;
    public int increment;
    public int type;
    private int numsplits;
    private boolean finalized;
    private boolean canhittarget;
    private Random rand;
    public long seed;
    public int particleAge;
    public int particleMaxAge;
    private AxisAlignedBB boundingBox;
    private World world;
    public Entity wrapper;
    public static final float speed = 3F;
    public static final int fadetime = 20;
    public static int playerdamage;
    public static int entitydamage;

    public LightningBoltCommon(World world1, WRVector3 wrvector3, WRVector3 wrvector3_1, long l)
    {
        type = 0;
        segments = new ArrayList();
        splitparents = new HashMap();
        canhittarget = true;
        world = world1;
        start = wrvector3;
        end = wrvector3_1;
        seed = l;
        rand = new Random(l);
        numsegments0 = 1;
        increment = 1;
        length = end.copy().sub(start).length();
        particleMaxAge = (3 + rand.nextInt(3)) - 1;
        multiplier = 1.0F;
        particleAge = -(int)(length * 3F);
        boundingBox = AxisAlignedBB.getBoundingBoxFromPool(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        boundingBox.setBB(AxisAlignedBB.getBoundingBoxFromPool(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.min(start.z, end.z), Math.max(start.x, end.x), Math.max(start.y, end.y), Math.max(start.z, end.z)).expand(length / 2.0F, length / 2.0F, length / 2.0F));
        segments.add(new Segment(end));
    }

    public LightningBoltCommon(World world1, Entity entity, Entity entity1, long l)
    {
        this(world1, new WRVector3(entity), new WRVector3(entity1), l);
    }

    public LightningBoltCommon(World world1, Entity entity, Entity entity1, long l, int i)
    {
        this(world1, new WRVector3(entity), new WRVector3(entity1.posX, (entity1.posY + (double)entity1.getEyeHeight()) - 0.69999998807907104D, entity1.posZ), l);
        increment = i;
        multiplier = 0.4F;
    }

    public LightningBoltCommon(World world1, TileEntity tileentity, Entity entity, long l)
    {
        this(world1, new WRVector3(tileentity), new WRVector3(entity), l);
    }

    public LightningBoltCommon(World world1, TileEntity tileentity, double d, double d1, double d2, long l)
    {
        this(world1, new WRVector3(tileentity), new WRVector3(d, d1, d2), l);
    }

    public LightningBoltCommon(World world1, double d, double d1, double d2,
            double d3, double d4, double d5, long l, int i, float f)
    {
        this(world1, new WRVector3(d, d1, d2), new WRVector3(d3, d4, d5), l);
        particleMaxAge = (i + rand.nextInt(i)) - i / 2;
        multiplier = f;
    }

    public LightningBoltCommon(World world1, double d, double d1, double d2,
            double d3, double d4, double d5, long l, int i, float f, int j)
    {
        this(world1, new WRVector3(d, d1, d2), new WRVector3(d3, d4, d5), l);
        particleMaxAge = (i + rand.nextInt(i)) - i / 2;
        multiplier = f;
        increment = j;
    }

    public void setWrapper(Entity entity)
    {
        wrapper = entity;
    }

    public void fractal(int i, float f, float f1, float f2, float f3)
    {
        if (finalized)
        {
            return;
        }
        ArrayList arraylist = segments;
        segments = new ArrayList();
        Iterator iterator = arraylist.iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            Segment segment1 = (Segment)iterator.next();
            Segment segment = segment1.prev;
            WRVector3 wrvector3 = segment1.diff.copy().scale(1.0F / (float)i);
            BoltPoint aboltpoint[] = new BoltPoint[i + 1];
            WRVector3 wrvector3_1 = segment1.startpoint.point;
            aboltpoint[0] = segment1.startpoint;
            aboltpoint[i] = segment1.endpoint;
            for (int j = 1; j < i; j++)
            {
                WRVector3 wrvector3_2 = WRVector3.getPerpendicular(segment1.diff).rotate(rand.nextFloat() * 360F, segment1.diff);
                wrvector3_2.scale((rand.nextFloat() - 0.5F) * f);
                WRVector3 wrvector3_3 = wrvector3_1.copy().add(wrvector3.copy().scale(j));
                aboltpoint[j] = new BoltPoint(wrvector3_2);
                aboltpoint[j].basepoint = wrvector3_3;
                aboltpoint[j].point = wrvector3_3.copy().add(wrvector3_2);
            }

            for (int k = 0; k < i; k++)
            {
                Segment segment2 = new Segment(aboltpoint[k + 1], segment1.light, segment1.segmentno * i + k, segment1.splitno);
                segment2.startpoint = aboltpoint[k];
                segment2.calcDiff();
                segment2.prev = segment;
                if (segment != null)
                {
                    segment.next = segment2;
                }
                if (k != 0 && rand.nextFloat() < f1)
                {
                    WRVector3 wrvector3_4 = WRVector3.xCrossProduct(segment2.diff).rotate(rand.nextFloat() * 360F, segment2.diff);
                    WRVector3 wrvector3_5 = segment2.diff.copy().rotate((rand.nextFloat() * 0.66F + 0.33F) * f3, wrvector3_4).scale(f2);
                    numsplits++;
                    splitparents.put(Integer.valueOf(numsplits), Integer.valueOf(segment2.splitno));
                    BoltPoint bp = new BoltPoint(aboltpoint[k + 1].offsetvec.copy().add(wrvector3_5));
                    bp.basepoint = aboltpoint[k + 1].basepoint;
                    bp.point = bp.basepoint.copy().add(bp.offsetvec);
                    Segment segment3 = new Segment(bp, segment1.light / 2.0F, segment2.segmentno, numsplits);
                    segment3.startpoint = aboltpoint[k];
                    segment3.calcDiff();
                    segment3.prev = segment;
                    segments.add(segment3);
                }
                segment = segment2;
                segments.add(segment2);
            }

            if (segment1.next != null)
            {
                segment1.next.prev = segment;
            }
        }
        while (true);
        numsegments0 *= i;
    }

    public void defaultFractal()
    {
        fractal(2, (length * multiplier) / 8F, 0.7F, 0.1F, 45F);
        fractal(2, (length * multiplier) / 12F, 0.5F, 0.1F, 50F);
        fractal(2, (length * multiplier) / 17F, 0.5F, 0.1F, 55F);
        fractal(2, (length * multiplier) / 23F, 0.5F, 0.1F, 60F);
        fractal(2, (length * multiplier) / 30F, 0.0F, 0.0F, 0.0F);
        fractal(2, (length * multiplier) / 34F, 0.0F, 0.0F, 0.0F);
        fractal(2, (length * multiplier) / 40F, 0.0F, 0.0F, 0.0F);
    }

    private void vecBBDamageSegment(WRVector3 wrvector3, WRVector3 wrvector3_1, ArrayList arraylist)
    {
        label0:
        {
            Vec3D vec3d = wrvector3.toVec3D();
            Vec3D vec3d1 = wrvector3_1.toVec3D();
            try
            {
                Iterator iterator = arraylist.iterator();
                do
                {
                    Entity entity;
                    do
                    {
                        if (!iterator.hasNext())
                        {
                            break label0;
                        }
                        entity = (Entity)iterator.next();
                    }
                    while (!(entity instanceof EntityLiving) || !entity.boundingBox.contract((entity.boundingBox.maxX - entity.boundingBox.minX) / 2.6000000000000001D, (entity.boundingBox.maxY - entity.boundingBox.minY) / 2.6000000000000001D, (entity.boundingBox.maxZ - entity.boundingBox.minZ) / 2.6000000000000001D).isVecInside(vec3d) && !entity.boundingBox.contract((entity.boundingBox.maxX - entity.boundingBox.minX) / 2.6000000000000001D, (entity.boundingBox.maxY - entity.boundingBox.minY) / 2.6000000000000001D, (entity.boundingBox.maxZ - entity.boundingBox.minZ) / 2.6000000000000001D).isVecInside(vec3d1));
                    if (wrapper == null || !(wrapper instanceof EntityLiving))
                    {
                        switch (type)
                        {
                            case 0:
                                entity.attackEntityFrom(DamageSource.magic, 1);
                                LightningBoltCommon.poisonBolt(entity, Potion.confusion, 2, world);
                                break;

                            case 1:
                                entity.attackEntityFrom(DamageSource.magic, 1);
                                entity.setFire(2);
                                break;

                            case 2:
                                entity.attackEntityFrom(DamageSource.magic, 1);
                                LightningBoltCommon.poisonBolt(entity, Potion.poison, 1, world);
                                break;

                            case 4:
                                entity.attackEntityFrom(DamageSource.magic, 3);
                                LightningBoltCommon.poisonBolt(entity, Potion.moveSlowdown, 2, world);
                                break;

                            case 5:
                                entity.attackEntityFrom(DamageSource.magic, 1);
                                LightningBoltCommon.poisonBolt(entity, Potion.moveSlowdown, 2, world);
                                break;
                        }
                    }
                    else
                    {
                        switch (type)
                        {
                            case 3:
                            default:
                                break;

                            case 0:
                                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(wrapper, wrapper), 1);
                                LightningBoltCommon.poisonBolt(entity, Potion.confusion, 2, world);
                                break;

                            case 1:
                                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(wrapper, wrapper), 1);
                                entity.setFire(2);
                                break;

                            case 2:
                                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(wrapper, wrapper), 1);
                                LightningBoltCommon.poisonBolt(entity, Potion.poison, 1, world);
                                break;

                            case 4:
                                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(wrapper, wrapper), (wrapper instanceof EntityPlayer) ? 4 : 2);
                                LightningBoltCommon.poisonBolt(entity, Potion.moveSlowdown, 2, world);
                                break;

                            case 5:
                                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(wrapper, wrapper), 1);
                                LightningBoltCommon.poisonBolt(entity, Potion.moveSlowdown, 2, world);
                                break;
                        }
                    }
                }
                while (true);
            }
            catch (Exception exception) { }
        }
    }

    private static void poisonBolt(Entity entity, Potion potion, int i, World world)
    {
        byte byte0 = 0;
        if (world.difficultySetting > 1)
        {
            if (world.difficultySetting == 2)
            {
                byte0 = 3;
            }
            else if (world.difficultySetting == 3)
            {
                byte0 = 6;
            }
        }
        if (byte0 > 0)
        {
            ((EntityLiving)entity).addPotionEffect(new PotionEffect(potion.id, byte0 * 20 * i, 0));
        }
    }

    private void bbTestEntityDamage()
    {
        if (type == 3 || type == 6)
        {
            return;
        }
        List list = world.getEntitiesWithinAABBExcludingEntity(wrapper, boundingBox);
        if (list.size() == 0)
        {
            return;
        }
        Segment segment;
        for (Iterator iterator = segments.iterator(); iterator.hasNext(); vecBBDamageSegment(segment.startpoint.point, segment.endpoint.point, (ArrayList)list))
        {
            segment = (Segment)iterator.next();
        }
    }

    private float rayTraceResistance(WRVector3 wrvector3, WRVector3 wrvector3_1, float f)
    {
        MovingObjectPosition movingobjectposition = world.rayTraceBlocks(wrvector3.toVec3D(), wrvector3_1.toVec3D());
        if (movingobjectposition == null)
        {
            return f;
        }
        if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
        {
            int i = world.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
            if (i == 0)
            {
                return f;
            }
            if (Block.blocksList[i] instanceof ISpecialResistance)
            {
                ISpecialResistance ispecialresistance = (ISpecialResistance)Block.blocksList[i];
                return f + (ispecialresistance.getSpecialExplosionResistance(world, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, wrvector3.x, wrvector3.y, wrvector3.z, wrapper) + 0.3F);
            }
            else
            {
                return f + (Block.blocksList[i].getExplosionResistance(wrapper) + 0.3F);
            }
        }
        else
        {
            return f;
        }
    }

    private void calculateCollisionAndDiffs()
    {
        HashMap hashmap = new HashMap();
        Collections.sort(segments, new SegmentSorter());
        int i = 0;
        int j = 0;
        float f = 0.0F;
        Iterator iterator = segments.iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            Segment segment1 = (Segment)iterator.next();
            if (segment1.splitno > i)
            {
                hashmap.put(Integer.valueOf(i), Integer.valueOf(j));
                i = segment1.splitno;
                j = ((Integer)hashmap.get(splitparents.get(Integer.valueOf(segment1.splitno)))).intValue();
                f = j < segment1.segmentno ? 50F : 0.0F;
            }
            if (f < 40F * segment1.light)
            {
                j = segment1.segmentno;
            }
        }
        while (true);
        hashmap.put(Integer.valueOf(i), Integer.valueOf(j));
        i = 0;
        j = ((Integer)hashmap.get(Integer.valueOf(0))).intValue();
        Segment segment;
        for (Iterator iterator1 = segments.iterator(); iterator1.hasNext(); segment.calcEndDiffs())
        {
            segment = (Segment)iterator1.next();
            if (i != segment.splitno)
            {
                i = segment.splitno;
                j = ((Integer)hashmap.get(Integer.valueOf(segment.splitno))).intValue();
            }
            if (segment.segmentno > j)
            {
                iterator1.remove();
            }
        }

        if (((Integer)hashmap.get(Integer.valueOf(0))).intValue() + 1 < numsegments0)
        {
            canhittarget = false;
        }
    }

    public void finalizeBolt()
    {
        if (finalized)
        {
            return;
        }
        else
        {
            finalized = true;
            calculateCollisionAndDiffs();
            Collections.sort(segments, new SegmentLightSorter());
            return;
        }
    }

    public void onUpdate()
    {
        particleAge += increment;
        if (particleAge > particleMaxAge)
        {
            particleAge = particleMaxAge;
        }
        bbTestEntityDamage();
    }
}

class SegmentSorter implements Comparator
{
    public int compare(Segment segment, Segment segment1)
    {
        int i = Integer.valueOf(segment.splitno).compareTo(Integer.valueOf(segment1.splitno));
        if (i == 0)
        {
            return Integer.valueOf(segment.segmentno).compareTo(Integer.valueOf(segment1.segmentno));
        }
        else
        {
            return i;
        }
    }

    public int compare(Object obj, Object obj1)
    {
        return compare((Segment)obj, (Segment)obj1);
    }
}

class SegmentLightSorter implements Comparator
{
    public int compare(Segment segment, Segment segment1)
    {
        return Float.compare(segment1.light, segment.light);
    }

    public int compare(Object obj, Object obj1)
    {
        return compare((Segment)obj, (Segment)obj1);
    }
}

class Segment
{
    public BoltPoint startpoint;
    public BoltPoint endpoint;
    public WRVector3 diff;
    public Segment prev;
    public Segment next;
    public WRVector3 nextdiff;
    public WRVector3 prevdiff;
    public float sinprev;
    public float sinnext;
    public float light;
    public int segmentno;
    public int splitno;

    public void calcDiff()
    {
        diff = endpoint.point.copy().sub(startpoint.point);
    }

    public void calcEndDiffs()
    {
        if (prev != null)
        {
            WRVector3 wrvector3 = prev.diff.copy().normalize();
            WRVector3 wrvector3_2 = diff.copy().normalize();
            prevdiff = wrvector3_2.add(wrvector3).normalize();
            sinprev = (float)Math.sin(WRVector3.anglePreNorm(wrvector3_2, wrvector3.scale(-1F)) / 2.0F);
        }
        else
        {
            prevdiff = diff.copy().normalize();
            sinprev = 1.0F;
        }
        if (next != null)
        {
            WRVector3 wrvector3_1 = next.diff.copy().normalize();
            WRVector3 wrvector3_3 = diff.copy().normalize();
            nextdiff = wrvector3_3.add(wrvector3_1).normalize();
            sinnext = (float)Math.sin(WRVector3.anglePreNorm(wrvector3_3, wrvector3_1.scale(-1F)) / 2.0F);
        }
        else
        {
            nextdiff = diff.copy().normalize();
            sinnext = 1.0F;
        }
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(startpoint.point.toString()))).append(" ").append(endpoint.point.toString()).toString();
    }

    public Segment(BoltPoint boltpoint1, float f, int i, int j)
    {
        startpoint = boltpoint1;
        endpoint = boltpoint1;
        light = f;
        segmentno = i;
        splitno = j;
        calcDiff();
    }

    public Segment(WRVector3 wrvector3_1)
    {
        this(new BoltPoint(wrvector3_1), 1.0F, 0, 0);
    }
}

class BoltPoint
{
    WRVector3 point;
    WRVector3 basepoint;
    WRVector3 offsetvec;

    public BoltPoint(WRVector3 wrvector3_1)
    {
        basepoint = new WRVector3(0.0D, 0.0D, 0.0D);
        point = basepoint.copy().add(wrvector3_1);
        offsetvec = wrvector3_1;
    }
}