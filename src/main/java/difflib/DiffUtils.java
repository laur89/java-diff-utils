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

import difflib.myers.Equalizer;
import difflib.myers.MyersDiff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * Implements the difference and patching engine
 *
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 * @version 0.4.1
 * @param T
 *            The type of the compared elements in the 'lines'.
 */
public class DiffUtils {

	/**
	 * Computes the difference between the original and revised list of elements
	 * with default diff algorithm
	 *
	 * @param original
	 *            The original text. Must not be {@code null}.
	 * @param revised
	 *            The revised text. Must not be {@code null}.
	 * @return The patch describing the difference between the original and
	 *         revised sequences. Never {@code null}.
	 */
	@Nonnull
	public static Patch diff(byte[] original, byte[] revised) {
		return DiffUtils.diff(original, revised, new MyersDiff());
	}

	/**
	 * Computes the difference between the original and revised list of elements
	 * with default diff algorithm
	 *
	 * @param original
	 *            The original text. Must not be {@code null}.
	 * @param revised
	 *            The revised text. Must not be {@code null}.
	 * @param algorithm
	 *            The diff algorithm. Must not be {@code null}.
	 * @return The patch describing the difference between the original and
	 *         revised sequences. Never {@code null}.
	 */
	@Nonnull
	public static Patch diff(byte[] original, byte[] revised, DiffAlgorithm algorithm) {
		if (original == null) {
			throw new IllegalArgumentException("original must not be null");
		}
		if (revised == null) {
			throw new IllegalArgumentException("revised must not be null");
		}
		if (algorithm == null) {
			throw new IllegalArgumentException("algorithm must not be null");
		}
		return algorithm.diff(original, revised);
	}

    /**
     * Patch the original text with given patch
     *
     * @param original
     *            the original text
     * @param patch
     *            the given patch
     * @return the revised text
     * @throws PatchFailedException
     *             if can't apply patch
     */
    @Nonnull
    public static List<Byte> patch(List<Byte> original, Patch patch) throws PatchFailedException {
        return patch( ArrayUtils.toPrimitive( original.toArray(new Byte[0]) ), patch );
    }

    /**
     * Patch the original text with given patch
     *
     * @param original
     *            the original text
     * @param patch
     *            the given patch
     * @return the revised text
     * @throws PatchFailedException
     *             if can't apply patch
     */
    @Nonnull
    public static List<Byte> patch(byte[] original, Patch patch) throws PatchFailedException {
        return patch.applyTo(original);
    }

	/**
	 * Unpatch the revised text for a given patch
	 *
	 * @param revised
	 *            the revised text
	 * @param patch
	 *            the given patch
	 * @return the original text
	 */
	public static List<Byte> unpatch(List<Byte> revised, Patch patch) {
		return patch.restore(revised);
	}

}
