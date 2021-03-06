package jmri.jmrit.display.controlPanelEditor;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.List;

import jmri.ConfigureManager;
import jmri.InstanceManager;
import jmri.JmriException;
import jmri.ShutDownManager;
import jmri.ShutDownTask;
import jmri.jmrit.logix.OBlock;
import jmri.jmrit.logix.OBlockManager;
import jmri.jmrit.logix.Portal;
import jmri.util.JUnitUtil;
import jmri.util.swing.JemmyUtil;

import org.junit.jupiter.api.*;
import org.junit.Assert;
import org.junit.Assume;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test simple functioning of the CircuitBuilder class.
 *
 * @author  Paul Bender Copyright (C) 2017
 * @author  Pete Cressman Copyright (C) 2019
 */
public class CircuitBuilderTest {

    ControlPanelEditor cpe;
    CircuitBuilder cb;

    @Test
    public void testCtor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        ControlPanelEditor f = new ControlPanelEditor();
        CircuitBuilder builder = new CircuitBuilder(f);
        Assert.assertNotNull("exists", builder);
        f.dispose();
        JUnitUtil.dispose(f);
    }

    @Test
    public void testOpenCBWindow() {
        getCPEandCB();

        cb.openCBWindow();
        cb.closeCBWindow();
    }


    @Test
    public void testEditCircuitFrame() {
        getCPEandCB();

        OBlock ob3 = InstanceManager.getDefault(OBlockManager.class).getOBlock("OB3");
        cb.setCurrentBlock(ob3);

        cb.editCircuit("editCircuitItem", false);
        Assert.assertNotNull("exists", cb.getEditFrame());
/*
        cb.editCircuit("editCircuitItem", true);
        JDialogOperator jdo = new JDialogOperator(Bundle.getMessage("editCircuitItem"));
        JTableOperator table = new JTableOperator(jdo);
        table.clickOnCell(3, 2);
        JButtonOperator button = new JButtonOperator(table, Bundle.getMessage("ButtonOpenCircuit"));
        button.doClick();
      */

        JFrameOperator editFrame = new JFrameOperator(cb.getEditFrame());
        JemmyUtil.pressButton(editFrame, Bundle.getMessage("ButtonDone"));
    }

    @Test
    public void testEditCircuitError() {
        getCPEandCB();

        cb.editCircuitError("OB1");

        JFrameOperator nfo = new JFrameOperator(cb.getEditFrame());
        JemmyUtil.pressButton(nfo, Bundle.getMessage("ButtonDone"));
    }

    @Test
    public void testEditPortals() {
        getCPEandCB();

        OBlock ob3 = InstanceManager.getDefault(OBlockManager.class).getOBlock("OB2");
        cb.setCurrentBlock(ob3);

        cb.editPortals("editPortalsItem", false);

        JFrameOperator nfo = new JFrameOperator(cb.getEditFrame());
        JemmyUtil.pressButton(nfo, Bundle.getMessage("ButtonDone"));
    }

    @Test
    public void testEditCircuitPaths() {
        getCPEandCB();

        OBlock ob3 = InstanceManager.getDefault(OBlockManager.class).getOBlock("OB3");
        cb.setCurrentBlock(ob3);

        cb.editCircuitPaths("editCircuitPathsItem", false);

        JFrameOperator nfo = new JFrameOperator(cb.getEditFrame());
        JemmyUtil.pressButton(nfo, Bundle.getMessage("ButtonDone"));
    }

    @Test
    public void testEditPortalDirection() {
        getCPEandCB();

        OBlock ob3 = InstanceManager.getDefault(OBlockManager.class).getOBlock("OB5");
        cb.setCurrentBlock(ob3);

        cb.editPortalDirection("editDirectionItem", false);

        JFrameOperator nfo = new JFrameOperator(cb.getEditFrame());
        JemmyUtil.pressButton(nfo, Bundle.getMessage("ButtonDone"));
    }

    @Test
    public void testEditSignalFrame() {
        getCPEandCB();

        OBlock ob3 = InstanceManager.getDefault(OBlockManager.class).getOBlock("OB4");
        cb.setCurrentBlock(ob3);

        cb.editSignalFrame("editSignalItem", false);

        JFrameOperator nfo = new JFrameOperator(cb.getEditFrame());
        JemmyUtil.pressButton(nfo, Bundle.getMessage("ButtonDone"));
    }

    @Test
    public void testEditPortalError() {
        getCPEandCB();
/*
        new Thread(() -> {
            JFrameOperator jfo = new JFrameOperator("Edit \"WestSiding\" Portals");
            JDialogOperator jdo = new JDialogOperator(jfo, Bundle.getMessage("incompleteCircuit"));
            JButtonOperator jbo = new JButtonOperator(jdo, "OK");
            jbo.push();
        }).start();*/

        cb.editPortalError("EastExit-EastJunction");

        JFrameOperator nfo = new JFrameOperator(cb.getEditFrame());
        JemmyUtil.pressButton(nfo, Bundle.getMessage("ButtonDone"));
        new org.netbeans.jemmy.QueueTool().waitEmpty(100);
        cpe.dispose();
    }

    @Test
    public void testEditPortalErrorIcon() throws Exception{
        getCPEandCB();

        OBlock block = InstanceManager.getDefault(OBlockManager.class).getByUserName("WestSiding");
        Portal portal = InstanceManager.getDefault(jmri.jmrit.logix.PortalManager.class).getPortal("Crapolla");
        new Thread(() -> {
            JFrameOperator jfo = new JFrameOperator("Edit \"WestSiding\" Portals");
            JDialogOperator jdo = new JDialogOperator(jfo, Bundle.getMessage("incompleteCircuit"));
            JButtonOperator jbo = new JButtonOperator(jdo, "OK");
            jbo.push();
        }).start();

        cb.editPortalError(block, portal, null);

        JFrameOperator nfo = new JFrameOperator(cb.getEditFrame());
        JemmyUtil.pressButton(nfo, Bundle.getMessage("ButtonDone"));
    }

    @Test
    @Disabled("Cannot get button pushed!")
    public void testNoBlock() {
        getCPEandCB();
        cb.editCircuitPaths("editCircuitPathsItem", false);

        JDialogOperator jdo = new JDialogOperator(Bundle.getMessage("NeedDataTitle"));
        JButtonOperator ok = new JButtonOperator(jdo, "OK");
        ok.push();
        //fixed: jmri.util.JUnitAppender.assertWarnMessage("getIconMap failed. family \"null\" not found in item type \"Portal\"");
    }

    void getCPEandCB() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        File f = new File("java/test/jmri/jmrit/display/controlPanelEditor/valid/CircuitBuilderTest.xml");
        try {
            InstanceManager.getDefault(ConfigureManager.class).load(f);
        } catch(JmriException je) {
            log.error("CircuitBuilderTest can't load CircuitBuilderTester.xml;", je);
        }
        cpe = (ControlPanelEditor) jmri.util.JmriJFrame.getFrame("CircuitBuilderTest Editor");
        Assert.assertNotNull("exists", cpe );
        cb = cpe.getCircuitBuilder();
        Assert.assertNotNull("exists", cb );
        //fixed: jmri.util.JUnitAppender.assertWarnMessage("getIconMap failed. family \"null\" not found in item type \"Portal\"");
    }

    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.resetProfileManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initOBlockManager();
    }

    @AfterEach
    public void tearDown() {
        if (cpe != null) {
            cpe.dispose();
        }
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.deregisterEditorManagerShutdownTask();
        if (InstanceManager.containsDefault(ShutDownManager.class)) {
            ShutDownManager sm = InstanceManager.getDefault(jmri.ShutDownManager.class);
            List<Runnable> rlist = sm.getRunnables();
            if (rlist.size() == 1 && rlist.get(0) instanceof jmri.jmrit.logix.WarrantShutdownTask) {
                sm.deregister((ShutDownTask)rlist.get(0));
            }
        }
        JUnitUtil.tearDown();
    }
    private final static Logger log = LoggerFactory.getLogger(CircuitBuilderTest.class);
}
