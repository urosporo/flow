package com.vaadin.tests.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import junit.framework.TestCase;
import org.mockito.Mockito;

import com.vaadin.flow.server.VaadinContext;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import com.vaadin.flow.server.startup.ApplicationConfiguration;
import com.vaadin.tests.util.MockUI;

public class SerializationTest extends TestCase {

    public void testSerializeVaadinSession_accessQueueIsRecreated()
            throws Exception {
        VaadinService vaadinService = new MockVaadinService(true);
        VaadinSession session = new VaadinSession(vaadinService);

        session = serializeAndDeserialize(session);

        assertNotNull(
                "Pending access queue was not recreated after deserialization",
                session.getPendingAccessQueue());
    }

    public void testSerializeVaadinSession_notProductionMode_disableDevModeSerialization_deserializedSessionHasNoUIs()
            throws Exception {
        VaadinSession session = serializeAndDeserializeWithUI(false);

        assertNotNull("UIs should be available after empty deserialization",
                session.getUIs());
        assertTrue("UIs should be empty after empty deserialization",
                session.getUIs().isEmpty());
    }

    public void testSerializeVaadinSession_notProductionMode_enableDevModeSerialization_deserializedSessionHasUI()
            throws Exception {
        VaadinSession session = serializeAndDeserializeWithUI(true);

        assertNotNull("UIs should be available after empty deserialization",
                session.getUIs());
        assertEquals(
                "UIs should contain a UI instance after empty deserialization",
                1, session.getUIs().size());
        assertEquals("Unexpected UI id after empty deserialization", 42,
                session.getUIs().iterator().next().getUIId());
    }

    private static VaadinSession serializeAndDeserializeWithUI(
            boolean serializeUI) throws IOException, ClassNotFoundException {
        VaadinService vaadinService = new MockVaadinService(false, serializeUI);
        VaadinSession session = new VaadinSession(vaadinService);
        // This is done only for test purpose to init the session lock,
        // should be called by Flow internally as soon as the session has
        // been created.
        session.refreshTransients(null, vaadinService);
        MockUI ui = new MockUI(session);
        ui.doInit(null, 42);
        session.addUI(ui);

        session = serializeAndDeserialize(session);
        // This is done only for test purpose to refresh the session lock,
        // should be called by Flow internally as soon as the session has
        // been retrieved from http session.
        session.refreshTransients(null, vaadinService);
        return session;
    }

    private static <S extends Serializable> S serializeAndDeserialize(S s)
            throws IOException, ClassNotFoundException {
        // Serialize and deserialize

        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bs);
        out.writeObject(s);
        byte[] data = bs.toByteArray();
        ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(data));
        @SuppressWarnings("unchecked")
        S s2 = (S) in.readObject();

        // using special toString(Object) method to avoid calling
        // Property.toString(), which will be temporarily disabled
        // TODO This is hilariously broken (#12723)
        if (s.equals(s2)) {
            System.out.println(toString(s) + " equals " + toString(s2));
        } else {
            System.out.println(toString(s) + " does NOT equal " + toString(s2));
        }

        return s2;
    }

    private static String toString(Object o) {
        return String.valueOf(o);
    }

    public static class Data implements Serializable {
        private String dummyGetter;
        private String dummyGetterAndSetter;
        private int dummyInt;

        public String getDummyGetterAndSetter() {
            return dummyGetterAndSetter;
        }

        public void setDummyGetterAndSetter(String dummyGetterAndSetter) {
            this.dummyGetterAndSetter = dummyGetterAndSetter;
        }

        public int getDummyInt() {
            return dummyInt;
        }

        public void setDummyInt(int dummyInt) {
            this.dummyInt = dummyInt;
        }

        public String getDummyGetter() {
            return dummyGetter;
        }
    }

    private static class MockVaadinService extends VaadinServletService {

        private final VaadinContext vaadinContext;
        private final boolean productionMode;
        private final boolean serialize;
        private final Lock lock = new ReentrantLock();

        {
            lock.lock();
        }

        public MockVaadinService(boolean productionMode) {
            super();
            this.vaadinContext = Mockito.mock(VaadinContext.class);
            this.productionMode = productionMode;
            serialize = false;
        }

        public MockVaadinService(boolean productionMode, boolean serialize) {
            super();
            this.vaadinContext = Mockito.mock(VaadinContext.class);
            this.productionMode = productionMode;
            this.serialize = serialize;
        }

        @Override
        public VaadinContext getContext() {
            ApplicationConfiguration applicationConfiguration = Mockito
                    .mock(ApplicationConfiguration.class);
            Mockito.when(
                    vaadinContext.getAttribute(Mockito.any(), Mockito.any()))
                    .thenReturn(applicationConfiguration);
            Mockito.when(applicationConfiguration.isProductionMode())
                    .thenReturn(productionMode);
            Mockito.when(applicationConfiguration
                    .isDevModeSessionSerializationEnabled())
                    .thenReturn(serialize);
            return vaadinContext;
        }

        @Override
        protected Lock getSessionLock(WrappedSession wrappedSession) {
            return lock;
        }

        @Override
        public String getMainDivId(VaadinSession session,
                VaadinRequest request) {
            return "main-div-id";
        }
    }

}
