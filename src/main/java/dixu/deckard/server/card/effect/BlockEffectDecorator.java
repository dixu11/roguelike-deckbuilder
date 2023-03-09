package dixu.deckard.server.card.effect;

public class BlockEffectDecorator extends EffectDecorator<BlockEffect> implements BlockEffect{
    public BlockEffectDecorator(BlockEffect decorated) {
        super(decorated);
    }
}
