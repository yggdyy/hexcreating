package pub.pigeon.yggdyy.hexcreating.blocks.board;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.HexItems;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;
import pub.pigeon.yggdyy.hexcreating.items.PrintedPaperItem;

import java.util.ArrayList;
import java.util.List;

public class AllBoardPasters {
    public interface BoardPaster {
        boolean canHandle(ItemStack stack, ServerWorld world);
        List<IItemStackMatcher> getResult(ItemStack stack, ServerWorld world);
    }
    @FunctionalInterface
    public interface IItemStackMatcher {
        List<Pair<ItemStack, ItemStack>> get();
    }
    public static class ItemStackMatcher implements IItemStackMatcher {
        public List<Pair<ItemStack, ItemStack>> list = new ArrayList<>(List.of());
        public void add(ItemStack stackIn, ItemStack stackOut) {
            list.add(new Pair<>(stackIn, stackOut));
        }
        @Override
        public List<Pair<ItemStack, ItemStack>> get() {
            return list;
        }
    }
    public static List<BoardPaster> pasters = new ArrayList<>(List.of());
    public static void add(BoardPaster plaster) {
        if(!pasters.contains(plaster)) pasters.add(plaster);
    }

    //add the plasters of hexcreating own
    public static void init() {
        //printed_paper
        add(new BoardPaster() {
            @Override
            public boolean canHandle(ItemStack stack, ServerWorld world) {
                return stack.isOf(ModItems.PRINTED_PAPER) && stack.hasNbt() && stack.getNbt().contains(PrintedPaperItem.IOTA_KEY);
            }
            @Override
            public List<IItemStackMatcher> getResult(ItemStack stack, ServerWorld world) {
                List<IItemStackMatcher> res = new ArrayList<>(List.of());
                var nbt = ModItems.PRINTED_PAPER.readIotaTag(stack);
                if(nbt == null) return res;
                ListIota list = (ListIota) IotaType.deserialize(nbt, world);
                for(Iota now : list.getList()) {
                    if(now instanceof PatternIota pIota) {
                        ItemStackMatcher matcher = new ItemStackMatcher();
                        String stroke = pIota.getPattern().anglesSignature();
                        ItemStack _s = new ItemStack(ModItems.SQUARE);
                        ModItems.SQUARE.writeDatum(_s, new PatternIota(pIota.getPattern()));
                        for(var dir : HexDir.values()) {
                            HexPattern p = HexPattern.fromAngles(stroke, dir);
                            ItemStack s = new ItemStack(ModItems.SQUARE);
                            ModItems.SQUARE.writeDatum(s, new PatternIota(p));
                            matcher.add(s, _s);
                        }
                        matcher.add(new ItemStack(ModItems.SQUARE), _s);
                        res.add(matcher);
                    }
                }
                return res;
            }
        });
        //slate
        add(new BoardPaster() {
            @Override
            public boolean canHandle(ItemStack stack, ServerWorld world) {
                return stack.isOf(HexItems.SLATE);
            }
            @Override
            public List<IItemStackMatcher> getResult(ItemStack stack, ServerWorld world) {
                ItemStackMatcher res = new ItemStackMatcher();
                var iota = HexItems.SLATE.readIota(stack, world);
                if(iota == null || !(iota instanceof PatternIota pIota)) return List.of(res);
                String stroke = pIota.getPattern().anglesSignature();
                ItemStack _s = new ItemStack(ModItems.SQUARE);
                ModItems.SQUARE.writeDatum(_s, new PatternIota(pIota.getPattern()));
                for(var dir : HexDir.values()) {
                    HexPattern p = HexPattern.fromAngles(stroke, dir);
                    ItemStack s = new ItemStack(ModItems.SQUARE);
                    ModItems.SQUARE.writeDatum(s, new PatternIota(p));
                    res.add(s, _s);
                }
                res.add(new ItemStack(ModItems.SQUARE), _s);
                return List.of(res);
            }
        });
        //focus
        add(new BoardPaster() {
            @Override
            public boolean canHandle(ItemStack stack, ServerWorld world) {
                return stack.isOf(HexItems.FOCUS);
            }
            @Override
            public List<IItemStackMatcher> getResult(ItemStack stack, ServerWorld world) {
                //unfinished
                return List.of();
            }
        });
    }
}
