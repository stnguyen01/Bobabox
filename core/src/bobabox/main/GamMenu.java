package bobabox.main;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import bobabox.main.Scratches.SctGuests;
import bobabox.main.Scratches.SctStackGuests;
import bobabox.main.Scratches.SctTap;
import bobabox.main.Screens.ScrGame;
import bobabox.main.Screens.ScrEnd;
import bobabox.main.Screens.ScrMenu;
import bobabox.main.Screens.ScrScratch;
import bobabox.main.Screens.ScrTut;
import bobabox.main.Scratches.SctWaiter;
import bobabox.main.Scratches.SctMultiGuests;


public class GamMenu extends Game {

    //https://youtu.be/D7u5B2Oh9r0
    public final int WORLD_WIDTH = 1000;
    public final int WORLD_HEIGHT = 500;
    private OrthographicCamera camera; // what's seen
    private StretchViewport viewport; //how it's seen

    int nScreen;
    ScrGame scrGame;
    ScrEnd scrEnd;
    ScrMenu scrMenu;
    SctTap sctTap;
    ScrTut scrTut;
    ScrScratch scrScratch;
    SctGuests sctGuests;
    SctWaiter sctWaiter;
    SctMultiGuests sctMultiGuests;
    SctStackGuests sctStackGuests;

    //Kieran's code (modified)
    public void updateScreen(int _nScreen) {
        nScreen = _nScreen;
        switch (nScreen) {
            case 0:
                scrGame.reset();
                setScreen(scrGame);
                break;
            case 1:
                setScreen(scrEnd);
                break;
            case 2:
                setScreen(scrMenu);
                break;
            case 3:
                setScreen(scrTut);
                break;
            case 4:
                setScreen(scrScratch);
                break;
            case 10:
                setScreen(sctTap);
                break;
            case 20:
                setScreen(sctGuests);
                break;
            case 30:
                setScreen(sctWaiter);
                break;
            case 40:
                setScreen(sctMultiGuests);
                break;
            case 50:
                setScreen(sctStackGuests);
                break;
            default:
                break;
        }
    }

    public void create() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        scrGame = new ScrGame(this, viewport, camera);
        scrEnd = new ScrEnd(this, viewport, camera);
        scrMenu = new ScrMenu(this, viewport, camera);
        scrTut = new ScrTut(this, viewport, camera);
        scrScratch = new ScrScratch(this, viewport, camera);
        sctTap = new SctTap(this);
        sctGuests = new SctGuests(this);
        sctWaiter = new SctWaiter(this);
        sctMultiGuests = new SctMultiGuests(this);
        sctStackGuests = new SctStackGuests(this);

        updateScreen(2);


    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
