package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import junit.framework.Assert;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimaryTorpedoStore;
  private TorpedoStore mockSecondaryTorpedoStore;

  @BeforeEach
  public void init(){
    mockPrimaryTorpedoStore = mock(TorpedoStore.class);
    mockSecondaryTorpedoStore = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPrimaryTorpedoStore, mockSecondaryTorpedoStore);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);

  }

  @Test
  public void fireTorpedo_Single_Success_When_PrimaryStoreIsEmpty(){
    //Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    Assert.assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_Success_When_SecondaryStoreIsEmpty(){
    //Arrange
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(false);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(anyInt());
    Assert.assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_Failure_When_AllStoresAreEmpty(){
    //Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    Assert.assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_Single_Success_FireTwiceAlternating(){
    //Arrange
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    //Act
    boolean firstFireResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondFireResult = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    Assert.assertEquals(true, firstFireResult);
    Assert.assertEquals(true, secondFireResult);
  }

  @Test
  public void fireTorpedo_Single_Success_FireTwice_When_SecondaryStoreIsEmpty(){
    //Arrange
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);

    //Act
    boolean firstFireResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondFireResult = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    verify(mockPrimaryTorpedoStore, times(2)).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    Assert.assertEquals(true, firstFireResult);
    Assert.assertEquals(true, secondFireResult);
  }

  @Test
  public void fireTorpedo_All_Success_When_SecondaryStoreIsEmpty(){
    //Arrange
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);
    
    //Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    Assert.assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_FireTwice_When_FirstStoreHasOnlyOneShot(){
    //Arrange
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true, false);
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false, true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);

    //Act
    boolean firstFireResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondFireResult = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    Assert.assertEquals(true, firstFireResult);
    Assert.assertEquals(false, secondFireResult);
  }

  @Test
  public void fireTorpedo_All_Success_When_FirstStoreIsEmpty(){
    //Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    //Assert
    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    Assert.assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Failure_When_StoresAreEmpty(){
    //Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    //Assert
    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    Assert.assertEquals(false, result);
  }

}
