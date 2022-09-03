package com.newjumper.oredustry.block;

import com.newjumper.oredustry.block.entity.HeatCableBlockEntity;
import com.newjumper.oredustry.block.entity.OredustryBlockEntities;
import com.newjumper.oredustry.util.OredustryTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static net.minecraft.world.level.block.PipeBlock.PROPERTY_BY_DIRECTION;

public class HeatCableBlock extends BaseEntityBlock {
    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;

    private static final VoxelShape CUBE = Block.box(5, 5, 5, 11, 11, 11);
    private static final VoxelShape ABOVE_CONNECTION = Block.box(6, 11, 6, 10, 16, 10);
    private static final VoxelShape BELOW_CONNECTION = Block.box(6, 0, 6, 10, 5, 10);
    private static final VoxelShape NORTH_CONNECTION = Block.box(6, 6, 0, 10, 10, 5);
    private static final VoxelShape EAST_CONNECTION = Block.box(11, 6, 6, 16, 10, 10);
    private static final VoxelShape SOUTH_CONNECTION = Block.box(6, 6, 11, 10, 10, 16);
    private static final VoxelShape WEST_CONNECTION = Block.box(0, 6, 6, 5, 10, 10);

    public HeatCableBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.FALSE).setValue(DOWN, Boolean.FALSE).setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE));
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        if(pState.hasBlockEntity()) return PushReaction.BLOCK;

        return super.getPistonPushReaction(pState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        BlockGetter level = pContext.getLevel();

        BlockState upState = level.getBlockState(pos.above());
        BlockState downState = level.getBlockState(pos.below());
        BlockState northState = level.getBlockState(pos.north());
        BlockState eastState = level.getBlockState(pos.east());
        BlockState southState = level.getBlockState(pos.south());
        BlockState westState = level.getBlockState(pos.west());

        return super.getStateForPlacement(pContext).setValue(UP, canConnect(upState)).setValue(DOWN, canConnect(downState)).setValue(NORTH, canConnect(northState)).setValue(EAST, canConnect(eastState)).setValue(SOUTH, canConnect(southState)).setValue(WEST, canConnect(westState));
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return pState.setValue(PROPERTY_BY_DIRECTION.get(pDirection), canConnect(pNeighborState));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    public boolean canConnect(BlockState pState) {
        return pState.is(OredustryTags.Blocks.HEAT_CONTAINER);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape shape = CUBE;

        if(pState.getValue(UP)) shape = Shapes.join(shape, ABOVE_CONNECTION, BooleanOp.OR);
        if(pState.getValue(DOWN)) shape = Shapes.join(shape, BELOW_CONNECTION, BooleanOp.OR);
        if(pState.getValue(NORTH)) shape = Shapes.join(shape, NORTH_CONNECTION, BooleanOp.OR);
        if(pState.getValue(EAST)) shape = Shapes.join(shape, EAST_CONNECTION, BooleanOp.OR);
        if(pState.getValue(SOUTH)) shape = Shapes.join(shape, SOUTH_CONNECTION, BooleanOp.OR);
        if(pState.getValue(WEST)) shape = Shapes.join(shape, WEST_CONNECTION, BooleanOp.OR);

        return shape;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    private int connections(BlockGetter level, BlockPos pos) {
        int count = 0;

        if(canConnect(level.getBlockState(pos.above()))) count++;
        if(canConnect(level.getBlockState(pos.below()))) count++;
        if(canConnect(level.getBlockState(pos.north()))) count++;
        if(canConnect(level.getBlockState(pos.east()))) count++;
        if(canConnect(level.getBlockState(pos.south()))) count++;
        if(canConnect(level.getBlockState(pos.west()))) count++;

        return count;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if(blockEntity instanceof HeatCableBlockEntity) {
            ArrayList<BlockPos> positions = new ArrayList<>();
            int cables = ((HeatCableBlockEntity) blockEntity).search(positions, pLevel, pPos);

            if(pLevel.isClientSide) System.out.println(cables);
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, OredustryBlockEntities.HEAT_CABLE.get(), HeatCableBlockEntity::tick);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new HeatCableBlockEntity(pPos, pState);
    }
}
