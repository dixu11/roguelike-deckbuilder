package dixu.deckard.server.effect;

public class BlockEffectDecorator extends EffectDecorator<BlockEffect> implements BlockEffect{
    public BlockEffectDecorator(BlockEffect decorated) {
        super(decorated);
    }
}
