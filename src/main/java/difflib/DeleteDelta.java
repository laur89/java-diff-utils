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

import java.lang.reflect.Array;
import java.util.List;

/**
 * Describes the delete-delta between original and revised texts.
 *
 * @param T The type of the compared elements in the 'lines'.
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 */
public class DeleteDelta extends Delta {

    /**
     * Creates a change delta with the two given chunks.
     *
     * @param original The original chunk. Must not be {@code null}.
     * @param revised  The original chunk. Must not be {@code null}.
     */
    public DeleteDelta( Chunk original, Chunk revised ) {
        super( original, revised );
    }

    /**
     * {@inheritDoc}
     *
     * @throws PatchFailedException
     */
    @Override
    public void applyTo( List<Byte> target ) throws PatchFailedException {
        verify( target );
        int position = getOriginal().getPosition();
        int size = getOriginal().size();
        for ( int i = 0; i < size; i++ ) {
            target.remove( position );
        }
    }

    @Override
    public void applyTo( byte[] target ) throws PatchFailedException {
        verify( target );

        final byte[] result = new byte[target.length - getOriginal().size()];

        if ( getOriginal().getPosition() == 0 ) {  // head will be cut
            System.arraycopy( target, getOriginal().size(), result, 0, target.length - getOriginal().size() );
        } else if ( getOriginal().getPosition() + getOriginal().size() == target.length ) { // tail will be cut
            System.arraycopy( target, 0, result, 0, target.length - getOriginal().size() );
        } else {
            System.arraycopy( target, 0, result, 0, getOriginal().getPosition() );  // copy head
            System.arraycopy( target, getOriginal().getPosition() + getOriginal().size(),
                    result, getOriginal().getPosition(), target.length - getOriginal().getPosition() + getOriginal().size() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restore( List<Byte> target ) {
        int position = this.getRevised().getPosition();
        byte[] lines = this.getOriginal().getLines();
        for (int i = 0; i < lines.length; i++) {
            target.add(position + i, lines[i]);
        }
    }

    @Override
    public void restore( byte[] target ) {
        System.arraycopy( this.getOriginal().getLines(), 0,
                target, this.getRevised().getPosition(), this.getOriginal().getLines().length );
    }

    @Override
    public TYPE getType() {
        return Delta.TYPE.DELETE;
    }

    @Override
    public void verify( List<Byte> target ) throws PatchFailedException {
        getOriginal().verify( target );
    }

    @Override
    public void verify( byte[] target ) throws PatchFailedException {
        getOriginal().verify( target );
    }

    @Override
    public String toString() {
        return "[DeleteDelta, position: " + getOriginal().getPosition() + ", lines: "
                + getOriginal().getLines() + "]";
    }
}
