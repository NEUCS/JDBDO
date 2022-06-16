package operators.mutation;

import java.io.Serializable;

public interface Mutation<I> extends Serializable {
    I execute(I individual);
}
