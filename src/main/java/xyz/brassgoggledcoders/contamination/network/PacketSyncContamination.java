package xyz.brassgoggledcoders.contamination.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import xyz.brassgoggledcoders.contamination.Contamination;
import xyz.brassgoggledcoders.contamination.api.IContaminationHolder;

//TODO Only sync those contaminations with clientside effects
public class PacketSyncContamination implements IMessage {

	public int chunkX, chunkZ;
	public NBTTagCompound holderTag;

	public PacketSyncContamination() {
	}

	public PacketSyncContamination(int chunkX, int chunkZ, NBTTagCompound holder) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		holderTag = holder;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		chunkX = buf.readInt();
		chunkZ = buf.readInt();
		holderTag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(chunkX);
		buf.writeInt(chunkZ);
		ByteBufUtils.writeTag(buf, holderTag);
	}

	public static class Handler implements IMessageHandler<PacketSyncContamination, IMessage> {

		@Override
		public IMessage onMessage(PacketSyncContamination message, MessageContext ctx) {
			Minecraft minecraft = Minecraft.getMinecraft();
			final WorldClient worldClient = minecraft.world;
			minecraft.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					Chunk chunk = worldClient.getChunk(message.chunkX, message.chunkZ);
					IContaminationHolder holder = chunk.getCapability(Contamination.CONTAMINATION_HOLDER_CAPABILITY,
							null);
					holder.readFromNBT(message.holderTag);
				}
			});
			return null;
		}

	}
}
