package org.terasology.entitySystem;

import org.junit.Before;
import org.junit.Test;
import org.terasology.entitySystem.event.AddComponentEvent;
import org.terasology.entitySystem.event.ChangedComponentEvent;
import org.terasology.entitySystem.event.RemovedComponentEvent;
import org.terasology.entitySystem.pojo.PojoEntityManager;
import org.terasology.entitySystem.stubs.IntegerComponent;
import org.terasology.entitySystem.stubs.StringComponent;

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Immortius <immortius@gmail.com>
 */
public class PojoEntityManagerTest {

    PojoEntityManager entityManager;
    
    @Before
    public void setup() {
        entityManager = new PojoEntityManager();
    }
    
    @Test
    public void addAndRetrieveComponent() {
        EntityRef entity = entityManager.createEntity();
        assertNotNull(entity);

        StringComponent comp = new StringComponent();
        entity.addComponent(comp);
        
        assertEquals(comp, entity.getComponent(StringComponent.class));
    }
    
    @Test
    public void removeComponent() {
        EntityRef entity = entityManager.createEntity();

        StringComponent comp = new StringComponent();
        entity.addComponent(comp);
        entity.removeComponent(StringComponent.class);

        assertNull(entity.getComponent(StringComponent.class));
    }
    
    @Test
    public void replaceComponent() {
        EntityRef entity = entityManager.createEntity();
        
        StringComponent comp = new StringComponent();
        comp.value = "Hello";
        StringComponent comp2 = new StringComponent();
        comp2.value = "Goodbye";
        
        entity.addComponent(comp);
        entity.addComponent(comp2);

        assertEquals(comp2, entity.getComponent(StringComponent.class));
    }

    @Test
    public void destroyEntity() {
        EntityRef entity = entityManager.createEntity();
        
        entity.addComponent(new StringComponent());
        entity.addComponent(new IntegerComponent());
        entity.destroy();

        assertNull(entity.getComponent(StringComponent.class));
        assertNull(entity.getComponent(IntegerComponent.class));
    }

    @Test
    public void iterateComponents() {
        EntityRef entity = entityManager.createEntity();
        StringComponent comp = new StringComponent();
        entity.addComponent(comp);

        for (Map.Entry<EntityRef, StringComponent> item : entityManager.iterateComponents(StringComponent.class)) {
            assertEquals(entity, item.getKey());
            assertEquals(comp, item.getValue());
        }
    }
    
    @Test
    public void changeComponentsDuringIterator() {
        EntityRef entity1 = entityManager.createEntity();
        entity1.addComponent(new StringComponent());
        EntityRef entity2 = entityManager.createEntity();
        entity2.addComponent(new StringComponent());
        
        Iterator<Map.Entry<EntityRef, StringComponent>> iterator = entityManager.iterateComponents(StringComponent.class).iterator();
        iterator.next();

        entity2.removeComponent(StringComponent.class);
        iterator.next();
    }

    @Test
    public void addComponentEventSent() {
        EventSystem eventSystem = mock(EventSystem.class);
        entityManager.setEventSystem(eventSystem);
        EntityRef entity1 = entityManager.createEntity();
        StringComponent comp = entity1.addComponent(new StringComponent());

        verify(eventSystem).send(entity1, AddComponentEvent.newInstance(), comp);
    }

    @Test
    public void removeComponentEventSent() {
        EventSystem eventSystem = mock(EventSystem.class);

        EntityRef entity1 = entityManager.createEntity();
        StringComponent comp = entity1.addComponent(new StringComponent());
        entityManager.setEventSystem(eventSystem);
        entity1.removeComponent(StringComponent.class);

        verify(eventSystem).send(entity1, RemovedComponentEvent.newInstance(), comp);
    }

    @Test
    public void changeComponentEventSentWhenSave() {
        EventSystem eventSystem = mock(EventSystem.class);

        EntityRef entity1 = entityManager.createEntity();
        StringComponent comp = entity1.addComponent(new StringComponent());
        entityManager.setEventSystem(eventSystem);
        entity1.saveComponent(comp);

        verify(eventSystem).send(entity1, ChangedComponentEvent.newInstance(), comp);
    }

    @Test
    public void changeComponentEventSentWhenAddOverExisting() {
        EventSystem eventSystem = mock(EventSystem.class);

        EntityRef entity1 = entityManager.createEntity();
        StringComponent comp1 = entity1.addComponent(new StringComponent());
        entityManager.setEventSystem(eventSystem);
        StringComponent comp2 = entity1.addComponent(new StringComponent());

        verify(eventSystem).send(entity1, ChangedComponentEvent.newInstance(), comp2);
    }
    
    @Test
    public void massRemovedComponentEventSentOnDestroy() {
        EventSystem eventSystem = mock(EventSystem.class);

        EntityRef entity1 = entityManager.createEntity();
        StringComponent comp1 = entity1.addComponent(new StringComponent());
        entityManager.setEventSystem(eventSystem);
        entity1.destroy();
        
        verify(eventSystem).send(entity1, RemovedComponentEvent.newInstance());
    }

}