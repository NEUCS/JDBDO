package operators.selection;

import java.io.Serializable;

public interface Selection<P, I> extends Serializable {
    I execute(P population);
}
