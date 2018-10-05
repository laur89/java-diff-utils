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

import java.util.List;

import javax.annotation.Nonnegative;

import static com.google.common.base.Preconditions.checkArgument;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Holds the information about the part of text involved in the diff process
 *
 * <p>
 * Text is represented as <code>Object[]</code> because the diff engine is
 * capable of handling more than plain ascci. In fact, arrays or lists of any
 * type that implements {@link java.lang.Object#hashCode hashCode()} and
 * {@link java.lang.Object#equals equals()} correctly can be subject to
 * differencing using this library.
 * </p>
 *
 * @param T The type of the compared elements in the 'lines'.
 * @author <a href="dm.naumenko@gmail.com>Dmitry Naumenko</a>
 */
@ToString
@EqualsAndHashCode
public class Chunk {

    @Nonnegative
    private final int position;
    private final byte[] lines;

    /**
     * Creates a chunk and asaves a copy of affected lines
     *
     * @param position the start position (zero-based numbering)
     * @param lines    the affected lines
     */
    public Chunk( @Nonnegative int position, byte[] lines ) {
        this.position = position;
        this.lines = lines;
    }

    /**
     * Verifies that this chunk's saved text matches the corresponding text in
     * the given sequence.
     *
     * @param target the sequence to verify against.
     */
    public void verify( byte[] target ) throws PatchFailedException {
        if ( last() > target.length ) {
            throw new PatchFailedException( "Incorrect Chunk: the position of chunk > target size" );
        }
        for ( int i = 0; i < size(); i++ ) {
            if ( target[position + i] != lines[i] ) {
                throw new PatchFailedException(
                        "Incorrect Chunk: the chunk content doesn't match the target" );
            }
        }
    }

    /**
     * Verifies that this chunk's saved text matches the corresponding text in
     * the given sequence.
     *
     * @param target the sequence to verify against.
     */
    public void verify( List<Byte> target ) throws PatchFailedException {
        if ( last() > target.size() ) {
            throw new PatchFailedException( "Incorrect Chunk: the position of chunk > target size" );
        }
        for ( int i = 0; i < size(); i++ ) {
            if ( target.get( position + i ) != lines[i] ) {
                throw new PatchFailedException(
                        "Incorrect Chunk: the chunk content doesn't match the target" );
            }
        }
    }

    /**
     * @return the start position of chunk in the text (zero-based numbering)
     */
    @Nonnegative
    public int getPosition() {
        return position;
    }

    /**
     * @return the affected lines
     */
    public byte[] getLines() {
        return lines;
    }

    @Nonnegative
    public int size() {
        return lines.length;
    }

    /**
     * Returns the index of the last line of the chunk. (zero-based numbering)
     */
    @Nonnegative
    public int last() {
        return getPosition() + size() - 1;
    }

}
