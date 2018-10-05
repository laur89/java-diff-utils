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
 * Describes the add-delta between original and revised texts.
 *
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 * @param T
 *            The type of the compared elements in the 'lines'.
 */
public class InsertDelta extends Delta {

	/**
	 * Creates an insert delta with the two given chunks.
	 *
	 * @param original
	 *            The original chunk. Must not be {@code null}.
	 * @param revised
	 *            The original chunk. Must not be {@code null}.
	 */
	public InsertDelta(Chunk original, Chunk revised) {
		super(original, revised);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws PatchFailedException
	 */
	@Override
	public void applyTo(List<Byte> target) throws PatchFailedException {
		verify(target);
		int position = this.getOriginal().getPosition();
		byte[] lines = this.getRevised().getLines();
		for (int i = 0; i < lines.length; i++) {
			// TODO: use addAll?
			target.add(position + i, lines[i]);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws PatchFailedException
	 */
	// TODO: return array instead?
	@Override
	public void applyTo(byte[] target) throws PatchFailedException {
		verify(target);

		target = ArrayUtils.insert(
			this.getOriginal().getPosition(), target, this.getRevised().getLines()
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restore(List<Byte> target) {
		int position = getRevised().getPosition();
		int size = getRevised().size();
		for (int i = 0; i < size; i++) {
			target.remove(position);
		}
	}


	@Override
	public void restore( byte[] target ) {
		final byte[] result = new byte[target.length - getRevised().size()];

		if ( getRevised().getPosition() == 0 ) {  // head will be cut
			System.arraycopy( target, getRevised().size(), result, 0, target.length - getRevised().size() );
		} else if ( getRevised().getPosition() + getRevised().size() == target.length ) { // tail will be cut
			System.arraycopy( target, 0, result, 0, target.length - getRevised().size() );
		} else {
			System.arraycopy( target, 0, result, 0, getRevised().getPosition() );  // copy head
			System.arraycopy( target, getRevised().getPosition() + getRevised().size(),
					result, getRevised().getPosition(), target.length - getRevised().getPosition() + getRevised().size() );
		}
	}

	@Override
	public void verify(List<Byte> target) throws PatchFailedException {
		if (getOriginal().getPosition() > target.size()) {
			throw new PatchFailedException("Incorrect patch for delta: "
					+ "delta original position > target size");
		}
	}

	@Override
	public void verify(byte[] target) throws PatchFailedException {
		if (getOriginal().getPosition() > target.length) {
			throw new PatchFailedException("Incorrect patch for delta: "
					+ "delta original position > target size");
		}
	}

	public TYPE getType() {
		return Delta.TYPE.INSERT;
	}

	@Override
	public String toString() {
		return "[InsertDelta, position: " + getOriginal().getPosition()
				+ ", lines: " + getRevised().getLines() + "]";
	}
}
