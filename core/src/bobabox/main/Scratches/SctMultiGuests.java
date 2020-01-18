package bobabox.main.Scratches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import java.util.ArrayList;
import java.util.List;
import bobabox.main.GamMenu;
import bobabox.main.Objects.ObjButton;
import bobabox.main.Objects.ObjTables;
import bobabox.main.Sprites.SprCustomer;

//Demonstrates multiple guests using arrays
public class SctMultiGuests implements Screen, InputProcessor {
    private GamMenu gamMenu;

    //Logic
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private StretchViewport viewport;
    private Vector2 vTouch;

    //Assets
    private Texture txtBG;
    private ObjButton btnHome;
    private ObjTables arTables[] = new ObjTables[3], objTable;
    private SprCustomer  sprCustomerS, sprCustMove, sprCustomerE;

    //Values
    private float fWORLD_WIDTH, fWORLD_HEIGHT;
    private boolean isSitting, bCustSat = false, isCstDragged = false, isTableClicked = false;
    private int nTimer = 0, nGst = 0, nTarget, nTable, nStatGst, nGstQueueTracker, nGstsSize;
    private List<SprCustomer> arliGuests;
    private List<SprCustomer> arliGuestsSat;
    private float fXG, fYG;
    private SprCustomer sprCst;
    int nTargetTble;

    public SctMultiGuests(GamMenu _gammenu) {
        gamMenu = _gammenu;

        //game height and width
        fWORLD_WIDTH = 1000;
        fWORLD_HEIGHT = 500;

        //Logic
        camera = new OrthographicCamera();
        vTouch = new Vector2();
        viewport = new StretchViewport(1000, 500, camera);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply();
        camera.setToOrtho(false);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); //camera looks at the center of the screen
        batch = new SpriteBatch();

        //table
        arTables[0] = new ObjTables(280, 80, "data/TABLE1_obj.png", "data/TABLE12_obj.png", viewport);
        arTables[1] = new ObjTables(fWORLD_WIDTH / 2, 50, "data/TABLE2_obj.png", "data/TABLE22_obj.png", viewport);
        arTables[2] = new ObjTables(fWORLD_WIDTH - 280, 80, "data/TABLE3_obj.png", "data/TABLE32_obj.png", viewport);
        fXG = 850;
        fYG = 175;

        //button
        txtBG = new Texture(Gdx.files.internal("data/Test_img.jpg"));
        btnHome = new ObjButton(900, 30, 260 / 2, 70 / 2, "data/HOME1_btn.png", "data/HOME2_btn.png", viewport);

        //guests
        arliGuests = new ArrayList<SprCustomer>();
        arliGuestsSat = new ArrayList<SprCustomer>();
        for (int i = 1; i <= 5; i++) {
            arliGuests.add(new SprCustomer("data/GUEST1_spr.png", batch));
        }
        nGstsSize = 5;
    }

    @Override
    public void render(float delta) {
        //set up
        camera.update();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        Gdx.input.setInputProcessor(this);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        nTimer++;

        //Drawing
        batch.draw(txtBG, 0, 0);
        btnHome.update(batch);
        updateTable();
        updateGuest(nGst, batch);
        if (isCstDragged) {
            sprCustMove.draw(batch);
            sprCustMove.hearts(objTable);
        }
        batch.end();

        //Buttons
        if (btnHome.justClicked()) {
            gamMenu.updateScreen(2);
        }

        //Timer for guests to enter
        if (nTimer % 300 == 0) {
            if (nGst < nGstsSize) {
                nGst++;
            }
            nTimer = 0;
        }

     /*   if (arliGuests.get(nTarget).isLeaving()) {
            isSitting = false;
        }*/
    }

    //Method runs through the array of tables
    private void updateTable() {
        for (int i = 0; i < 1; i++) {
            objTable = arTables[i]; //temp. table
            objTable.draw(batch);
            if (isTableClicked){
                nTargetTble = i;

            }
        }
    }

    //Runs all of the SprCustomers' functions
    private void updateGuest(int nGst, SpriteBatch batch) {
        for (int n = 0; n < nGst; n++) {
            sprCustomerE = arliGuests.get(n); //temporary Guest
            sprCustomerE.draw(batch);
            sprCustomerE.entering(nGst, n, bCustSat);
            sprCustomerE.hearts(objTable);
        }

        for (int z = 0; z < arliGuestsSat.size(); z++) {
            sprCustomerS = arliGuestsSat.get(z); //temporary Customer 'S'at
            sprCustomerS.updateStatus();
            sprCustomerS.hearts(objTable);
            nStatGst = arliGuestsSat.get(nTarget).updateStatus();
            if (nStatGst >= 6) {
                isSitting = false;
            }
        }

    }


    @Override
    public void dispose() {
        txtBG.dispose();
        batch.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(1000 / 2, 500 / 2, 0);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        viewport.unproject(vTouch.set(Gdx.input.getX(), Gdx.input.getY()));
        for (int n = 0; n < arliGuests.size(); n++) {
            sprCst = arliGuests.get(n); //temporary Guest

            if (sprCst.getBoundingRectangle().contains(vTouch)) {
                nTarget = n;
                isCstDragged = true;
                sprCustMove = arliGuests.get(nTarget);
                arliGuests.remove(sprCustMove);
                nGst = nGst - 1;
                nGstQueueTracker--;
            }
        }
        nGstsSize = arliGuests.size();

// Checks if mouse is over table and clicked
        if (objTable.getBoundingRectangle().contains(vTouch)) {
            ObjTables objTableServed;

            objTableServed = arTables[nTargetTble];

            fXG = Math.round(objTableServed.getX() + (objTableServed.getWidth() / 2 - 40));
            fYG = Math.round(objTableServed.getY() + objTableServed.getHeight());

            isTableClicked = true;

        }
        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        //When a customer is sat at an available table
        if (isCstDragged) {
            if (sprCustMove.getBoundingRectangle().overlaps(arTables[nTable].getBoundingRectangle())) {
                isSitting = true;
                arTables[nTable].isAvb(isSitting, arliGuestsSat.size()); //<< should now be !isAvb
                arliGuestsSat.add(sprCustMove);

            }
        }

        for (int n = 0; n < nGstsSize; n++) {
            bCustSat = true;
        }

        //not in use, please ignore all hard coding :D
        for (int t = 0; t <= 2; t++) {
            if (arTables[t].getBoundingRectangle().contains(vTouch)) {
                //Checks which table was being clicked
             //   System.out.println("TABLE " + t + " WAS CLICKED");
            }
        }

        isCstDragged = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (sprCustMove.getBoundingRectangle().contains(vTouch)) {
            sprCustMove.drag(vTouch, viewport);
            isCstDragged = true;
        } else {
            isCstDragged = false;
        }

        for (int i = 0; i < 3; i++) {
            if (sprCustMove.getBoundingRectangle().overlaps(arTables[i].getBoundingRectangle())) {
                nTable = i;
            }
        }

        return false;
    }


    //----------------------------------------------------------------------------------------------------------------
    //FUNCTIONS NOT IN USE
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
