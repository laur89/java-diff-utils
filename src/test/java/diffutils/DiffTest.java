package diffutils;

import static org.assertj.core.api.Assertions.assertThat;

import difflib.*;
import junit.framework.TestCase;

public class DiffTest extends TestCase {

    public void testDiff_Insert() {
        final Patch patch = DiffUtils.diff( new byte[] { -33 }, new byte[] { -33, 20, 9 } );
        assertNotNull( patch );
        assertEquals( 1, patch.getDeltas().size() );
        final Delta delta = patch.getDeltas().get( 0 );
        assertEquals( InsertDelta.class, delta.getClass() );

        assertThat( delta.getOriginal() )
                .hasFieldOrPropertyWithValue( "position", 1 )
                .hasFieldOrPropertyWithValue( "lines", new byte[0] );
        assertThat( delta.getRevised() )
                .hasFieldOrPropertyWithValue( "position", 1 )
                .hasFieldOrPropertyWithValue( "lines", new byte[] { 20, 9 } );
        // also test equals:
        assertEquals( new Chunk( 1, new byte[0] ), delta.getOriginal() );
        assertEquals( new Chunk( 1, new byte[] { 20, 9 } ), delta.getRevised() );
    }

    public void testDiff_Delete() {
        final Patch patch = DiffUtils.diff( new byte[] { -33, 2, -1 }, new byte[] { -1 } );
        assertNotNull( patch );
        assertEquals( 1, patch.getDeltas().size() );
        final Delta delta = patch.getDeltas().get( 0 );
        assertEquals( DeleteDelta.class, delta.getClass() );

        assertThat( delta.getOriginal() )
                .hasFieldOrPropertyWithValue( "position", 0 )
                .hasFieldOrPropertyWithValue( "lines", new byte[] { -33, 2 } );
        assertThat( delta.getRevised() )
                .hasFieldOrPropertyWithValue( "position", 0 )
                .hasFieldOrPropertyWithValue( "lines", new byte[0] );
        // also test equals:
        assertEquals( new Chunk( 0, new byte[] { -33, 2 } ), delta.getOriginal() );
        assertEquals( new Chunk( 0, new byte[0] ), delta.getRevised() );
    }

    public void testDiff_Change() {
        final byte[] changeTest_from = new byte[] { -33, 2, -1 };
        final byte[] changeTest_to = new byte[] { -33, 13, -1 };

        final Patch patch = DiffUtils.diff( changeTest_from, changeTest_to );
        assertNotNull( patch );
        assertEquals( 1, patch.getDeltas().size() );
        final Delta delta = patch.getDeltas().get( 0 );
        assertEquals( ChangeDelta.class, delta.getClass() );

        assertThat( delta.getOriginal() )
                .hasFieldOrPropertyWithValue( "position", 1 )
                .hasFieldOrPropertyWithValue( "lines", new byte[] { 2 } );
        assertThat( delta.getRevised() )
                .hasFieldOrPropertyWithValue( "position", 1 )
                .hasFieldOrPropertyWithValue( "lines", new byte[] { 13 } );
        // also test equals:
        assertEquals( new Chunk( 1, new byte[] { 2 } ), delta.getOriginal() );
        assertEquals( new Chunk( 1, new byte[] { 13 } ), delta.getRevised() );
    }

    public void testDiff_EmptyList() {
        final Patch patch = DiffUtils.diff( new byte[0], new byte[0] );
        assertNotNull( patch );
        assertEquals( 0, patch.getDeltas().size() );
    }

    public void testDiff_EmptyListWithNonEmpty() {
        final Patch patch = DiffUtils.diff( new byte[0], new byte[] { 5 } );
        assertNotNull( patch );
        assertEquals( 1, patch.getDeltas().size() );
        final Delta delta = patch.getDeltas().get( 0 );
        assertEquals( InsertDelta.class, delta.getClass() );
    }
}
