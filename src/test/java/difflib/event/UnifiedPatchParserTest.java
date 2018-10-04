package difflib.event;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Test;

import difflib.Delta;
import difflib.Patch;

public class UnifiedPatchParserTest {

    @Test
    public void test() throws Exception {
        final AtomicInteger visited = new AtomicInteger( 0 );
        PatchHandler<String> handler = new PatchHandler<String>() {
            @Override
            public void handle( String originalPath, String revisedPath, Patch<String> patch ) {
                visited.incrementAndGet();

                Delta<String> delta = patch.getDeltas().get( 0 );
                assertThat( delta.getOriginal().getPosition() ).isEqualTo( 0 );
                if ( originalPath.startsWith( "test/a.txt" ) ) {
                    assertThat( revisedPath ).startsWith( "test2/a.txt" );
                    assertThat( patch.getDeltas() ).hasSize( 1 );
                    assertThat( delta.getOriginal().getLines() ).containsExactly( "hello, world" );
                    assertThat( delta.getRevised().getLines() ).containsOnly( "hello" );
                } else {
                    assertThat( originalPath ).startsWith( "test/b.txt" );
                    assertThat( revisedPath ).startsWith( "test2/b.txt" );
                    assertThat( patch.getDeltas() ).hasSize( 1 );
                    assertThat( delta.getOriginal().getLines() ).containsExactly( "hello! 1", "hello! 2" );
                    assertThat( delta.getRevised().getLines() ).containsOnly( "hello" );
                }
            }
        };

        new UnifiedPatchParser().parse( new File( "src/test/resources/event", "2files.diff" ), handler );
        assertThat( visited.intValue() ).isEqualTo( 2 );
    }
}
