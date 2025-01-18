package pub.pigeon.yggdyy.hexcreating.mixins;

import at.petrak.hexcasting.api.casting.circles.CircleExecutionState;
import org.spongepowered.asm.mixin.Mixin;


//i failed to use this well
//maybe one day I will come back to complete this
@Mixin(CircleExecutionState.class)
public class CircleExecutionStateInjector {

    /**
     * @author yggdyy_
     * @reason to add CircleAmplifierSender check into this method
     */
    /*@Overwrite
    public static Result<CircleExecutionState, @Nullable BlockPos> createNew(BlockEntityAbstractImpetus impetus, @Nullable ServerPlayerEntity caster) {
        //the following are the original content of this method
        var level = (ServerWorld) impetus.getWorld();

        if (level == null)
            return new Result.Err<>(null);

        // Flood fill! Just like VCC all over again.
        // this contains tentative positions and directions entered from
        var todo = new Stack<Pair<Direction, BlockPos>>();
        todo.add(Pair.of(impetus.getStartDirection(), impetus.getPos().offset(impetus.getStartDirection())));
        var seenGoodPosSet = new HashSet<BlockPos>();
        var seenGoodPositions = new ArrayList<BlockPos>();

        while (!todo.isEmpty()) {
            var pair = todo.pop();
            var enterDir = pair.getFirst();
            var herePos = pair.getSecond();

            var hereBs = level.getBlockState(herePos);
            if (!(hereBs.getBlock() instanceof ICircleComponent cmp)) {
                continue;
            }
            if (!cmp.canEnterFromDirection(enterDir, herePos, hereBs, level)) {
                continue;
            }

            if (seenGoodPosSet.add(herePos)) {
                // it's new
                seenGoodPositions.add(herePos);
                var outs = cmp.possibleExitDirections(herePos, hereBs, level);
                for (var out : outs) {
                    todo.add(Pair.of(out, herePos.offset(out)));
                }
            }
        }

        if (seenGoodPositions.isEmpty()) {
            return new Result.Err<>(null);
        } else if (!seenGoodPosSet.contains(impetus.getPos())) {
            // we can't enter from the side the directrix exits from, so this means we couldn't loop back.
            // the last item we tried to examine will always be a terminal slate (b/c if it wasn't,
            // then the *next* slate would be last qed)
            return new Result.Err<>(seenGoodPositions.get(seenGoodPositions.size() - 1));
        }

        var knownPositions = new HashSet<>(seenGoodPositions);
        var reachedPositions = new ArrayList<BlockPos>();
        reachedPositions.add(impetus.getPos());
        var start = seenGoodPositions.get(0);

        FrozenPigment colorizer = null;
        UUID casterUUID;
        if (caster == null) {
            casterUUID = null;
        } else {
            colorizer = HexAPI.instance().getColorizer(caster);
            casterUUID = caster.getUuid();
        }

        //the following are the added content
        var hc_ip = impetus.getPos();
        List<BlockPos> hc_posiblePos = new ArrayList<>();
        hc_posiblePos.add(hc_ip.add(1, 0, 0));
        hc_posiblePos.add(hc_ip.add(-1, 0, 0));
        hc_posiblePos.add(hc_ip.add(0, 1, 0));
        hc_posiblePos.add(hc_ip.add(0, -1, 0));
        hc_posiblePos.add(hc_ip.add(0, 0, 1));
        hc_posiblePos.add(hc_ip.add(0, 0, -1));

        for(int hc_i = 0; hc_i < hc_posiblePos.size(); ++hc_i) {
            var hc_np = hc_posiblePos.get(hc_i);
            if(impetus.hasWorld() && impetus.getWorld().getBlockEntity(hc_np) instanceof CircleAmplifierSenderBlockEntity hc_ne) {
                knownPositions.addAll(hc_ne.getAvailableReceiversPos());
            }
        }
        var retCes =

        return new Result.Ok<>(
                new CircleExecutionState(impetus.getPos(), impetus.getStartDirection(), knownPositions,
                        reachedPositions, start, impetus.getStartDirection(), new CastingImage(), casterUUID, colorizer));


    }*/
}
