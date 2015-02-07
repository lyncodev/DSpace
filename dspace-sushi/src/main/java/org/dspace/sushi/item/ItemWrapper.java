package org.dspace.sushi.item;

import org.dspace.content.Item;
import org.dspace.content.Metadatum;
import org.niso.schemas.counter.Identifier;
import org.niso.schemas.counter.IdentifierType;

import java.util.ArrayList;
import java.util.Collection;

public class ItemWrapper {
    private static final String DC_PUBLISHER = "dc.publisher";

    private final Item item;

    public ItemWrapper(Item item) {
        this.item = item;
    }


    public String getName() {
        return item.getName();
    }

    public String getPublisher() {
        return item.getMetadata(DC_PUBLISHER);
    }

    public Collection<Identifier> identifiers () {
        Collection<Identifier> result = new ArrayList<>();
        Metadatum[] metadata = item.getMetadata("dc", "identifier", Item.ANY, Item.ANY);
        for (final Metadatum metadatum : metadata) {
            if (metadatum.qualifier != null) {
                try {
                    final IdentifierType identifierType = IdentifierType.fromValue(metadatum.qualifier.toUpperCase());
                    result.add(new Identifier() {{ setValue(metadatum.value); setType(identifierType); }});
                } catch (IllegalArgumentException e) {

                }
            }
        }
        return result;
    }
}
