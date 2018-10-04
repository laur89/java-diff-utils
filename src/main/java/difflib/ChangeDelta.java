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

import org.apache.commons.lang3.ArrayUtils;

/**
 * Describes the change-delta between original and revised texts.
 *
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 * @param T The type of the compared elements in the 'lines'.
 */
public class ChangeDelta<T> extends Delta<T> {

    /**
     * Creates a change delta with the two given chunks.
     * @param original The original chunk. Must not be {@code null}.
     * @param revised The original chunk. Must not be {@code null}.
     */
    public ChangeDelta(Chunk<T> original, Chunk<T>revised) {
    	super(original, revised);
    }

    /**
     * {@inheritDoc}
     *
     * @throws PatchFailedException
     */
    @Override
    public void applyTo(List<T> target) throws PatchFailedException {
        verify(target);
        int position = getOriginal().getPosition();
        int size = getOriginal().size();
        for (int i = 0; i < size; i++) {
            target.remove(position);
        }
        int i = 0;
        for (T line : getRevised().getLines()) {
            target.add(position + i, line);
            i++;
        }
    }

    @Override
    public void applyTo( T[] target ) throws PatchFailedException {
        verify(target);

        final T[] result = (T[]) Array.newInstance( target.getClass().getComponentType(), target.length - getOriginal().size() );

        if ( getOriginal().getPosition() == 0 ) {  // head will be cut
            System.arraycopy( target, getOriginal().size(), result, 0, target.length - getOriginal().size() );
        } else if ( getOriginal().getPosition() + getOriginal().size() == target.length ) { // tail will be cut
            System.arraycopy( target, 0, result, 0, target.length - getOriginal().size() );
        } else {
            System.arraycopy( target, 0, result, 0, getOriginal().getPosition() );  // copy head
            System.arraycopy( target, getOriginal().getPosition() + getOriginal().size(),
                    result, getOriginal().getPosition(), target.length - getOriginal().getPosition() + getOriginal().size() );
        }

        target = ArrayUtils.insert(
                this.getOriginal().getPosition(), target, this.getRevised().getLines()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restore(List<T> target) {
        int position = getRevised().getPosition();
        int size = getRevised().size();
        for (int i = 0; i < size; i++) {
            target.remove(position);
        }
        int i = 0;
        for (T line : getOriginal().getLines()) {
            target.add(position + i, line);
            i++;
        }
    }

    @Override
    public void restore( T[] target ) {
        final T[] result = (T[]) Array.newInstance( target.getClass().getComponentType(), target.length - getRevised().size() );

        if ( getRevised().getPosition() == 0 ) {  // head will be cut
            System.arraycopy( target, getRevised().size(), result, 0, target.length - getRevised().size() );
        } else if ( getRevised().getPosition() + getRevised().size() == target.length ) { // tail will be cut
            System.arraycopy( target, 0, result, 0, target.length - getRevised().size() );
        } else {
            System.arraycopy( target, 0, result, 0, getRevised().getPosition() );  // copy head
            System.arraycopy( target, getRevised().getPosition() + getRevised().size(),
                    result, getRevised().getPosition(), target.length - getRevised().getPosition() + getRevised().size() );
        }

        target = ArrayUtils.insert(
                this.getRevised().getPosition(), target, this.getOriginal().getLines()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verify(List<T> target) throws PatchFailedException {
//        getOriginal().verify(target);
//        if (getOriginal().getPosition() > target.size()) {
//            throw new PatchFailedException("Incorrect patch for delta: "
//                    + "delta original position > target size");
//        }
    }

    @Override
    public void verify( T[] target ) throws PatchFailedException {
        getOriginal().verify(target);
        if (getOriginal().getPosition() > target.length) {
            throw new PatchFailedException("Incorrect patch for delta: "
                    + "delta original position > target size");
        }
    }

    @Override
    public String toString() {
        return "[ChangeDelta, position: " + getOriginal().getPosition() + ", lines: "
                + getOriginal().getLines() + " to " + getRevised().getLines() + "]";
    }

    @Override
    public TYPE getType() {
        return Delta.TYPE.CHANGE;
    }
}
