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

import lombok.EqualsAndHashCode;

/**
 * Describes the delta between original and revised texts.
 *
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 */
@EqualsAndHashCode
public abstract class Delta {

    /**
     * The original chunk.
     */
    private Chunk original;

    /**
     * The revised chunk.
     */
    private Chunk revised;

    /**
     * Specifies the type of the delta.
     */
    public enum TYPE {
        /**
         * A change in the original.
         */
        CHANGE,
        /**
         * A delete from the original.
         */
        DELETE,
        /**
         * An insert into the original.
         */
        INSERT
    }

    /**
     * Construct the delta for original and revised chunks
     *
     * @param original Chunk describing the original text. Must not be {@code null}.
     * @param revised  Chunk describing the revised text. Must not be {@code null}.
     */
    public Delta( Chunk original, Chunk revised ) {
        if ( original == null ) {
            throw new IllegalArgumentException( "original must not be null" );
        } else if ( revised == null ) {
            throw new IllegalArgumentException( "revised must not be null" );
        }

        this.original = original;
        this.revised = revised;
    }

    public abstract void verify( List<Byte> target ) throws PatchFailedException;

    public abstract void applyTo( List<Byte> target ) throws PatchFailedException;

    public abstract void applyTo( byte[] target ) throws PatchFailedException;

    public abstract void restore( List<Byte> target );

    public abstract void verify( byte[] target ) throws PatchFailedException;

    public abstract void restore( byte[] target );

    /**
     * Returns the type of delta
     *
     * @return the type enum
     */
    public abstract TYPE getType();

    /**
     * @return The Chunk describing the original text.
     */
    public Chunk getOriginal() {
        return original;
    }

    /**
     * @param original The Chunk describing the original text to set.
     */
    public void setOriginal( Chunk original ) {
        this.original = original;
    }

    /**
     * @return The Chunk describing the revised text.
     */
    public Chunk getRevised() {
        return revised;
    }

    /**
     * @param revised The Chunk describing the revised text to set.
     */
    public void setRevised( Chunk revised ) {
        this.revised = revised;
    }

}
