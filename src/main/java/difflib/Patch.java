/*
   Copyright 2010 Dmitry Naumenko (dm.naumenko@gmail.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package difflib;

import java.util.*;

import static com.google.common.primitives.Bytes.asList;

/**
 * Describes the patch holding all deltas between the original and revised texts.
 *
 * @param T The type of the compared elements in the 'lines'.
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 */
public class Patch {
    private List<Delta> deltas = new LinkedList<>();

    /**
     * Apply this patch to the given target
     *
     * @return the patched text
     * @throws PatchFailedException if can't apply patch
     */
    public List<Byte> applyTo( final List<Byte> target ) throws PatchFailedException {
        return applyTo( new LinkedList<>( target ) );
    }

    /**
     * Apply this patch to the given target
     *
     * @return the patched text
     * @throws PatchFailedException if can't apply patch
     */
    public List<Byte> applyTo( final LinkedList<Byte> target ) throws PatchFailedException {
        ListIterator<Delta> it = getDeltas().listIterator( deltas.size() );
        while ( it.hasPrevious() ) {
            Delta delta = it.previous();
            delta.applyTo( target );
        }
        return target;
    }

    /**
     * Apply this patch to the given target
     *
     * @return the patched text
     * @throws PatchFailedException if can't apply patch
     */
    public List<Byte> applyTo( byte[] target ) throws PatchFailedException {
        return applyTo( new LinkedList<>( asList(target) ) );
    }

    /**
     * Restore the text to original. Opposite to applyTo() method.
     *
     * @param target the given target
     * @return the restored text
     */
    public List<Byte> restore( List<Byte> target ) {
        List<Byte> result = new LinkedList<>( target );
        ListIterator<Delta> it = getDeltas().listIterator( deltas.size() );
        while ( it.hasPrevious() ) {
            Delta delta = it.previous();
            delta.restore( result );
        }
        return result;
    }

    /**
     * Add the given delta to this patch
     *
     * @param delta the given delta
     */
    public void addDelta( Delta delta ) {
        deltas.add( delta );
    }

    /**
     * Get the list of computed deltas
     *
     * @return the deltas
     */
    public List<Delta> getDeltas() {
        Collections.sort( deltas, DeltaComparator.INSTANCE );
        return deltas;
    }
}
