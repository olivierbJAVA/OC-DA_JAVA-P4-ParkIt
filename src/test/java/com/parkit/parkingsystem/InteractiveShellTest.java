package com.parkit.parkingsystem;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class InteractiveShellTest {

	private static InteractiveShell interactiveShellUnderTest;

	@Mock
	private static InputReaderUtil inputReaderUtilMock;

	//@Mock
	//private static ParkingSpotDAO parkingSpotDAOMock;

	//@Mock
	//private static TicketDAO ticketDAOMock;

	@Mock
	private static ParkingService parkingServiceMock;

	@Disabled("Ne fonctionne pas : impossible de mocker les collaborateurs internes à la classe InteractiveShell car ils ne sotn pas passés en paramètre (pas de constructeur ou de setters)")
	@Test
	public void interactiveShellTest_WhenReadSelectionEquals1() {

		Mockito.when(inputReaderUtilMock.readSelection()).thenReturn(1);

		Mockito.doNothing().when(parkingServiceMock).processIncomingVehicle();

		interactiveShellUnderTest = new InteractiveShell();

		//parkingServiceMock=new ParkingService(inputReaderUtilMock, parkingSpotDAOMock, ticketDAOMock);

		//parkingServiceMock =new ParkingService(inputReaderUtilMock, new ParkingSpotDAO(), new TicketDAO());

		//Mockito.when(inputReaderUtilMock.readSelection()).thenReturn(new Integer(1));

		//parkingServiceMock=new ParkingService(inputReaderUtilMock, parkingSpotDAOMock, ticketDAOMock);

		interactiveShellUnderTest.loadInterface();

		Mockito.verify(parkingServiceMock).processIncomingVehicle();

	}

}
