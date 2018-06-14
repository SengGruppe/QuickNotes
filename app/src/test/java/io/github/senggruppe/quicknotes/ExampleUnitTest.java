package io.github.senggruppe.quicknotes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    /* Context wird nicht erstellt(Null exception).
    @Moc

    static Context ctx;

    @BeforeClass
    public static void listPrepare() throws IOException, ClassNotFoundException {
        for (int i=0;i<10;i++){
            DataStore.getNotes(ctx).add(new Note(i+". Note"));
        }
    }

    @Test
    public void listIsSame() throws IOException, ClassNotFoundException {
        Iterator<Note> i=DataStore.getNotes(ctx).iterator();
        Iterator<Label> j=DataStore.getLabels().iterator();
        while(i.hasNext()){
            while(j.hasNext())
                if(j.next().notes.contains(i.next())) throw new IllegalStateException("Note wurde nicht im Label gefunden.");
            j=DataStore.getLabels().iterator();
        }

        while(j.hasNext()){
            while(i.hasNext())
                if(i.next().labels.contains(j.next())) throw new IllegalStateException("Label wurde nicht im Note gefunden.");
            i=DataStore.getNotes(ctx).iterator();
        }
    }
    */
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}