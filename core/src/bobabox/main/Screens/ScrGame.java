package bobabox.main.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;
import java.util.List;

import bobabox.main.Objects.ObjBar;
import bobabox.main.Objects.ObjButton;
import bobabox.main.GamMenu;
import bobabox.main.Sprites.SprCustomer;
import bobabox.main.Objects.ObjTables;
import bobabox.main.Sprites.SprServer;

public class ScrGame implements Screen, InputProcessor {
    private GamMenu gamMenu;

    //Values
    private Vector2 vTouch;
    private boolean bHasOrder, isCstDragged = false; //boolean for server
    private boolean isTableClicked = false, isSitting, bCustSat = false, bTempHere = false; //boolean for guests
    private int nW, nH, nGameTimer = 60, nTable; //int for game
    private int nFPS, nStatGst, nClickedBar = 0, nGstQueueTracker; //int for server
    private int nTimer = 0, nGst = 0, nTarget, nGstsSize; //int for guests
    private float fXG, fYG;
    private int nTargetTble; // int for tables
    //Logic
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private SpriteBatch batch;
    //Assets
    private Texture txtBg, txtStats;
    private List<SprCustomer> arliGuests, arliGuestsSat;
    private SprCustomer sprCustomerE, sprCst, sprCustMove, sprCustomerS;
    private SprServer sprServer;
    private ObjTables arTables[] = new ObjTables[3], objTable;
    private ObjTables objTableServed;
    private ObjButton btnPause;
    private BitmapFont bfFont;
    private ObjBar objBar;

    public ScrGame(GamMenu _gamMenu, StretchViewport _viewport, OrthographicCamera _camera) {
        gamMenu = _gamMenu;

        //game world
        nW = gamMenu.WORLD_WIDTH;
        nH = gamMenu.WORLD_HEIGHT;
        vTouch = new Vector2();
        viewport = _viewport;
        viewport.apply();
        camera = _camera;
        camera.setToOrtho(false);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0); //camera looks at the center of the screen
        resize(nW, nH);
        batch = new SpriteBatch();

        //assets
        objBar = new ObjBar(viewport, new Rectangle(300, 300, 450, 80));
        txtBg = new Texture("data/GameBG_img.png");
        txtStats = new Texture("data/STATS_img.png");
        btnPause = new ObjButton(940, 40, 90, 55, "data/PAUSE1_btn.png", "data/PAUSE2_btn.png", viewport);
        bfFont = new BitmapFont(Gdx.files.internal("data/gamefnt32.fnt"));
        bfFont.setColor(Color.DARK_GRAY);
        bfFont.getData().setScale(1f, 1f);

        //table
        arTables[0] = new ObjTables(280, 80, "data/TABLE1_obj.png", "data/TABLE12_obj.png", viewport);
        arTables[1] = new ObjTables(nW / 2, 50, "data/TABLE2_obj.png", "data/TABLE22_obj.png", viewport);
        arTables[2] = new ObjTables(nW - 280, 80, "data/TABLE3_obj.png", "data/TABLE32_obj.png", viewport);
        fXG = 850;
        fYG = 175;

        //guests
        nGstsSize = 5;
        arliGuests = new ArrayList<SprCustomer>();
        arliGuestsSat = new ArrayList<SprCustomer>();
        for (int i = 1; i <= 5; i++) {
            arliGuests.add(new SprCustomer("data/GUEST1_spr.png", batch));
        }

        //server
    }

    @Override
    public void render(float delta) {
        //SetUp
        camera.update();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        Gdx.input.setInputProcessor(this);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Initial drawing
        batch.draw(txtBg, 0, 0, nW, nH);
        sprServer.update(fXG, fYG, batch, isCstDragged);
        btnPause.update(batch);
        bfFont.draw(batch, Integer.toString(nGameTimer), nW - 100, nH - 138);

        //Game Timer (1 min. round)
        nFPS++;
        nTimer++;
        if (nFPS % 60 == 0) {
            nGameTimer--;
        }
        if (nGameTimer == 0) {
            //Sends player to ScrEnd
            gamMenu.updateScreen(1);
        }


        //Checks if Bar is clicked (for Waiter)
        if (objBar.isTapped()) {
            nClickedBar++;
            fXG = objBar.rBar().x + 1;
            fYG = objBar.rBar().y - 20;
            sprServer.update(fXG, fYG, batch, isCstDragged);
        }

        //Checks if Table is clicked (for Waiter)
        if (isTableClicked) {
            sprServer.update(fXG, fYG, batch, isCstDragged);

            //if the table has a customer sitting there, attend to their needs
            if (!objTableServed.isAvb(isSitting, objTableServed.giveCstI())) {
                sprServer.service(batch, nClickedBar, arliGuestsSat.get(objTableServed.giveCstI()));
                nStatGst = arliGuestsSat.get(objTableServed.giveCstI()).updateStatus();
                System.out.println("Status: " + nStatGst + " for SprCustomer:" + objTableServed.giveCstI());

            }
        }

        updateTable();
        updateGuest(nGst, batch);

      /* if (isCstDragged) {
            sprCustMove.draw(batch);
            sprCustMove.hearts(objTable);

        }*/

        if (bTempHere) {
            sprCustMove.draw(batch);
            sprCustMove.hearts(objTable);
        }

        //Fonts for game status + timer
        batch.draw(txtStats, nW - 200, nH - 165, 200, 150);
        bfFont.draw(batch, Integer.toString(nGameTimer) + "s", nW - 100, nH - 135);
        bfFont.draw(batch, "0.00*", nW - 120, nH - 80); //*temporarily placing a hard value

        batch.end();

        //Timer for Guests to enter
        if (nTimer % 300 == 0 && nGstQueueTracker <= 2) {
            if (nGst < nGstsSize) {
                nGst++;
                nGstQueueTracker++; // Tracks the wait line allowing only 3 customers to go be in line
            }
            nTimer = 0;
        }

        //ObjButton
        checkButtons();
        if (btnPause.isMousedOver() && Gdx.input.isTouched()) {
            // System.out.println("Pause");
            gamMenu.updateScreen(1);
        }

    }

    //Method runs through the array of tables
    private void updateTable() {
        for (int i = 0; i < 3; i++) {
            objTable = arTables[i]; //temp. table
            objTable.draw(batch);
        }
    }

    //Runs all of the SprCustomers' functions
    private void updateGuest(int nGst, SpriteBatch batch) {
        for (int n = 0; n < nGst; n++) {
            sprCustomerE = arliGuests.get(n); //temporary Customer 'E'ntered
            sprCustomerE.draw(batch);
            sprCustomerE.entering(nGst, n, bCustSat);
            sprCustomerE.hearts(objTable);
        }
        for (int z = 0; z < arliGuestsSat.size(); z++) {
            sprCustomerS = arliGuestsSat.get(z); //temporary Customer 'S'at
            sprCustomerS.updateStatus();
            sprCustomerS.hearts(objTable);

            if (nStatGst >= 6) {
                isSitting = false;
            }
        }
    }


    public void reset() { //Resets the game's cached values
        sprServer = new SprServer(850, 175);
        isTableClicked = false;
        nFPS = 0;
        bHasOrder = false;
        isCstDragged = false;
        isSitting = false;
        bCustSat = false;
        nGameTimer = 60;
        nClickedBar = 0;
        nTimer = 0;
        nGst = 0;
    }

    // Checks if buttons are pressed
    private void checkButtons() {
        if (btnPause.justClicked()) {
            gamMenu.updateScreen(2);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(nW / 2, nH / 2, 0);
    }

    @Override
    public void dispose() {
        txtBg.dispose();
        batch.dispose();
    }


    @Override //Input processor
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        viewport.unproject(vTouch.set(Gdx.input.getX(), Gdx.input.getY()));

        //For Tables
        for (int i = 0; i < 3; i++) {
            objTable = arTables[i]; //temp. table
            if (objTable.getBoundingRectangle().contains(vTouch)) {
                nTargetTble = i;
                objTableServed = arTables[nTargetTble];

                fXG = Math.round(objTableServed.getX() + (objTableServed.getWidth() / 2 - 40));
                fYG = Math.round(objTableServed.getY() + objTableServed.getHeight());
                //System.out.println("TABLE " + i + " WAS CLICKED");

                isTableClicked = true;
            }
        }

        //for Customers
        // ** bTempHere checks if a temporary guest is present.
        // ** Prevents dragging of next customer in line

        if(!bTempHere) {
            for (int n = 0; n < arliGuests.size(); n++) {
                sprCst = arliGuests.get(n); //temporary Guest

                if (sprCst.getBoundingRectangle().contains(vTouch)) {
                    nTarget = n;
                    isCstDragged = true;
                    sprCustMove = arliGuests.get(nTarget);
                    bTempHere = true;
                    arliGuests.remove(sprCustMove);
                    nGst = nGst - 1;
                    nGstQueueTracker--;
                }
            }
        }
        nGstsSize = arliGuests.size();

        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        //When a customer is sat at an available table
        if (isCstDragged) {
            if (sprCustMove.getBoundingRectangle().overlaps(arTables[nTable].getBoundingRectangle())) {
                isSitting = true;
                arTables[nTable].isAvb(isSitting, arliGuestsSat.size()); //sets table to unavailable and gives table the customer
                arliGuestsSat.add(sprCustMove);
                bTempHere = false;
            }
        }

        for (int n = 0; n < nGstsSize; n++) {
            bCustSat = true;
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


    //-------------------------------------------------------------------------------------------------------------
    //VOIDS NOT IN USE
    @Override
    public void show() {
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

    //BOOLEANS NOT IN USE
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
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
