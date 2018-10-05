package diffutils;

import static org.apache.commons.lang3.ArrayUtils.toObject;
import static org.assertj.core.api.Assertions.assertThat;

import difflib.DiffUtils;
import difflib.Patch;
import junit.framework.TestCase;

public class PatchTest extends TestCase {

    public void testPatch_Insert() throws Exception {
        final byte[] insertTest_from = new byte[] { -31 };
        final byte[] insertTest_to = new byte[] { -31, 2, -1, 7 };

        final Patch patch = DiffUtils.diff( insertTest_from, insertTest_to );
        assertThat( DiffUtils.patch( insertTest_from, patch ) )
                .containsExactly( toObject( insertTest_to ) );
    }

    public void testPatch_Delete() throws Exception {
        final byte[] deleteTest_from = new byte[] { -31, 2, -9, 7 };
        final byte[] deleteTest_to = new byte[] { -9 };

        final Patch patch = DiffUtils.diff( deleteTest_from, deleteTest_to );
        assertThat( DiffUtils.patch( deleteTest_from, patch ) )
                .containsExactly( toObject( deleteTest_to ) );
    }

    public void testPatch_Change() throws Exception {
        final byte[] changeTest_from = new byte[] { -31, 2, -9, 7 };
        final byte[] changeTest_to = new byte[] { -31, 4, 2, 7 };

        final Patch patch = DiffUtils.diff( changeTest_from, changeTest_to );
        assertThat( DiffUtils.patch( changeTest_from, patch ) )
                .containsExactly( toObject( changeTest_to ) );
    }
}
