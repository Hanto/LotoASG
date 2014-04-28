package UI;

import static datos.CentroDeDatos.listaTiposSorteo;
import static datos.Constantes.ALTURA_FILAS_BOTES;
import static datos.Constantes.ALTURA_FILAS_ESCRUTINIOS;
import static datos.Constantes.ALTURA_FILAS_PADING;
import static datos.Constantes.ALTURA_FILAS_RESULTADOS;
import static datos.Constantes.COLOR_BOTESFECHA_BASE;
import static datos.Constantes.COLOR_BOTESFECHA_SOMBRA;
import static datos.Constantes.COLOR_BOTES_BASE;
import static datos.Constantes.COLOR_BOTES_SOMBRA;
import static datos.Constantes.COLOR_CATEGORIA_BASE;
import static datos.Constantes.COLOR_CATEGORIA_SOMBRA;
import static datos.Constantes.COLOR_EXTRA_BASE;
import static datos.Constantes.COLOR_EXTRA_SOMBRA;
import static datos.Constantes.COLOR_RESULTADOS_BASE;
import static datos.Constantes.COLOR_RESULTADOS_SOMBRA;
import static datos.Constantes.ORDEN_RENDER;
import static utils.Text.printText;

import java.text.DecimalFormat;

import screen.PantallaResultados;
import sorteos.Sorteo;
import sorteos.Sorteo.Escrutinio;
import sorteos.TiposSorteo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.me.Game.Loto;

import datos.CentroDeDatos;
import datos.Recursos;

public class GeneraTablas 
{	
	
	public static void generarResultadosSemana (Table tabla)
	{
		tabla.clear();
		for (int i=0;i<ORDEN_RENDER.length;i++)
		{
			for (int j=0; j<listaTiposSorteo.size;j++)
			{
				if (ORDEN_RENDER[i].toLowerCase().equals(listaTiposSorteo.get(j).getNombre().toLowerCase())) 
				{ 
					generarSorteos(listaTiposSorteo.get(j).getListaSorteos(), listaTiposSorteo.get(j).getNumSorteosSemanales(), true, true, tabla );
					break;
				}
			}
		}
		generarDisclaimer(tabla);
	}
	
	public static void generarResultadosTrimestrales (final Sorteo sorteo, Table tabla)
	{
		tabla.clear();
		//Buscamos el tipo de Sorteo al que corresponde el sorteo que hemos clickado
		TiposSorteo tSorteo = sorteo.getTipoSorteo();
		generarSorteos(tSorteo.getListaSorteos(), tSorteo.getListaSorteos().size, false, false, tabla );
		generarDisclaimer(tabla);
	}
	
	public static void generarSorteos (Array<Sorteo> listaSorteos, int numSorteos, boolean mostrarCabecera, boolean mostrarDiaFecha, Table tabla)
	{	
		generarCabeceraSorteo(listaSorteos.get(0), mostrarCabecera, true, tabla);
				
		for (int i=0; i<listaSorteos.size && i<numSorteos;i++)	
		{
			//cada tipo de sorteo invoca su propio generador para componer su linea de resultados:
			Group lineaSorteo = listaSorteos.get(i).getTipoSorteo().generador.generarLineaSorteo(listaSorteos.get(i), mostrarDiaFecha);
			tabla.add(lineaSorteo).left().height(lineaSorteo.getHeight()-ALTURA_FILAS_PADING*2);
			tabla.row();
		}
	}
	
	public static void generarCabeceraSorteo (final Sorteo sorteo, boolean mostrarCabecera, boolean irAHistorico, Table tabla)
	{
		tabla.add(generarCabeceraSorteo (sorteo, mostrarCabecera, irAHistorico)).left().height(ALTURA_FILAS_RESULTADOS-ALTURA_FILAS_PADING);
		tabla.row();
	}
	
	public static Group generarCabeceraSorteo (final Sorteo sorteo, boolean mostrarCabecera, boolean irAHistorico)
	{
		Group group = new Group();
		
		if (mostrarCabecera)
		{
			//Barra de Fondo Sorteo
			Image barra = Recursos.getBarraAzulOscuro();
			group.addActor(barra);
			barra.setBounds(0, 0, PantallaResultados.anchoPantalla, ALTURA_FILAS_RESULTADOS);
						
			//Icono del Sorteo
			Image iconoSorteo = Recursos.getLogo(sorteo.getNombreSorteo().toLowerCase());
			group.addActor(iconoSorteo);
			iconoSorteo.setPosition(5, ALTURA_FILAS_RESULTADOS/2-22);
			
			//Nombre Sorteo
			printText(sorteo.getNombreSorteoAPP(), PantallaResultados.fuenteCabecera, Color.WHITE, Color.BLACK, 55, ALTURA_FILAS_RESULTADOS/2+8, Align.left, Align.center, 2, group);
			
			//Informacion Bote
			if (sorteo.getTieneBote() && sorteo.getBoteActivo())
				{ printText("Bote en juego de "+sorteo.getResultadoBotes(2)+" €", PantallaResultados.fuente20, Color.WHITE, Color.BLACK, 55, 5, Align.left, Align.bottom, 2, group); }
			
			//Flecha de desplazamiento a Historico:
			Image iconoTrimestre = Recursos.getFlecha();
			group.addActor(iconoTrimestre);
			
			if (irAHistorico)
			{
				iconoTrimestre.setPosition(PantallaResultados.anchoPantalla-42, ALTURA_FILAS_RESULTADOS/2-(int)(iconoTrimestre.getHeight()/2));
				printText("Histórico", PantallaResultados.fuente20, Color.WHITE, Color.BLACK, PantallaResultados.anchoPantalla-37, ALTURA_FILAS_RESULTADOS/2+8, Align.right, Align.center, 2, group);
				printText("Trimestral", PantallaResultados.fuente20, Color.WHITE, Color.BLACK, PantallaResultados.anchoPantalla-37, ALTURA_FILAS_RESULTADOS/2-8, Align.right, Align.center, 2, group);
			}
			else
			{
				iconoTrimestre.setOrigin(iconoTrimestre.getWidth()/2, iconoTrimestre.getHeight()/2);
				iconoTrimestre.setRotation(180);
				iconoTrimestre.setPosition(PantallaResultados.anchoPantalla-42, ALTURA_FILAS_RESULTADOS/2-iconoTrimestre.getHeight()/2);
				printText("Volver", PantallaResultados.fuente20, Color.WHITE, Color.BLACK, PantallaResultados.anchoPantalla-37, ALTURA_FILAS_RESULTADOS/2-10, Align.right, Align.center, 2, group);
			}	
		}
		if (!mostrarCabecera)
		{
			Image barra = Recursos.getBarraAzulClaro();
			group.addActor(barra);
			barra.setBounds(0, 0, PantallaResultados.anchoPantalla, ALTURA_FILAS_RESULTADOS);
		}
				
		//Evento de Click
		Listeners.listenerCabeceraSorteo(sorteo, group);
		
		return group;
	}
		
	public static void generarEscrutinio (Sorteo sorteo, Table tablaEscrutinio)
	{							
		generarCabeceraEscrutinio(sorteo, tablaEscrutinio);
				
		if (sorteo.getListaEscrutinio().size == 0)
		{ 
			tablaEscrutinio.add(generarLineaEscrutinio()).left().height(ALTURA_FILAS_ESCRUTINIOS-ALTURA_FILAS_PADING*0);
			tablaEscrutinio.row();
		}
		else for (int i=0; i<sorteo.getListaEscrutinio().size; i++)
		{
			if (sorteo.getListaEscrutinio().get(i).tipoSorteo.equals("JokerPrimitiva") && i == 7)
			{
				tablaEscrutinio.add(generarLeyendaCabeceraEscrutinio("Categoría","Premio","Joker")).left().height(ALTURA_FILAS_ESCRUTINIOS-ALTURA_FILAS_PADING*0);
				tablaEscrutinio.row();
			}
			if (sorteo.getListaEscrutinio().get(i).tipoSorteo.equals("JokerPrimitiva") && i > 6) 
			{
				String Joker = sorteo.getListaEscrutinio().get(i).Acertantes;
				if (Joker.length() < (14-i) )
				{
					for (int j=Joker.length();j<14-i;j++) { Joker = "0"+Joker; }
					sorteo.getListaEscrutinio().get(i).Acertantes = Joker;
				}
				tablaEscrutinio.add(generarLineaEscrutinio(sorteo.getListaEscrutinio().get(i), false)).left().height(ALTURA_FILAS_ESCRUTINIOS-ALTURA_FILAS_PADING*0);
				tablaEscrutinio.row();
			}
			else
			{
				tablaEscrutinio.add(generarLineaEscrutinio(sorteo.getListaEscrutinio().get(i), true)).left().height(ALTURA_FILAS_ESCRUTINIOS-ALTURA_FILAS_PADING*0);
				tablaEscrutinio.row();
			}
		}
		generarDisclaimer(tablaEscrutinio);
	}
	
	public static void generarCabeceraEscrutinio (Sorteo sorteo, Table tablaCabecera)
	{
		Array<Sorteo> aSorteo = new Array<Sorteo>();
		aSorteo.add(sorteo);
		
		generarSorteos(aSorteo, 1, false, false, tablaCabecera);
		//tablaCabecera.row();
			
		Group linea2 = generarLeyendaCabeceraEscrutinio ("Categoría", "Premio", "Acertantes");
		
		tablaCabecera.add(linea2).left().height(ALTURA_FILAS_ESCRUTINIOS-ALTURA_FILAS_PADING*2);
		tablaCabecera.row();
		
		Listeners.listenerCabeceraLineaEscrutinio(linea2);
	}
	
	public static Group generarLeyendaCabeceraEscrutinio (String categoria, String premio, String acertantes)
	{
		Group linea = new Group ();
		
		Image barra = Recursos.getBarraAzulOscuro();		
		linea.addActor(barra);
		barra.setBounds(0, 0, PantallaResultados.anchoPantalla, ALTURA_FILAS_ESCRUTINIOS);
		
		printText(categoria, PantallaResultados.fuente30, Color.WHITE, Color.BLACK, 5, ALTURA_FILAS_ESCRUTINIOS/2, Align.left, Align.center, 2, linea);
		printText(premio, PantallaResultados.fuente30, Color.WHITE, Color.BLACK, 320, ALTURA_FILAS_ESCRUTINIOS/2, Align.right, Align.center, 2, linea);
		printText(acertantes, PantallaResultados.fuente30, Color.WHITE, Color.BLACK, 475, ALTURA_FILAS_ESCRUTINIOS/2, Align.right, Align.center, 2, linea);
				
		Listeners.listenerCabeceraLineaEscrutinio(linea);
		
		return linea;
	}
	
	public static Group generarLineaEscrutinio (Escrutinio escrutinio, Boolean acertantesEsNumero)
	{
		Group group = new Group();
		Image barra = Recursos.getBarraAzulClaro();
		group.addActor(barra); 
		barra.setBounds(0, 0, PantallaResultados.anchoPantalla, ALTURA_FILAS_ESCRUTINIOS);
		
		double premioNumber = Double.parseDouble(escrutinio.Premio);
		
		DecimalFormat formatoPremio = new DecimalFormat("#,##0.00");
		double acertantesNumber = Double.parseDouble(escrutinio.Acertantes);
		DecimalFormat formatoAcertantes = new DecimalFormat ("#,###");
		
		//Categoria:
		if ( escrutinio.Categoria.length() > 22) 
			{ printText(escrutinio.Categoria, PantallaResultados.font14, COLOR_CATEGORIA_BASE, COLOR_CATEGORIA_SOMBRA, 5, ALTURA_FILAS_ESCRUTINIOS/2, Align.left, Align.center, 1, group); }
		else if (escrutinio.Categoria.length() > 19 )
			{ printText(escrutinio.Categoria, PantallaResultados.font20, COLOR_CATEGORIA_BASE, COLOR_CATEGORIA_SOMBRA, 5, ALTURA_FILAS_ESCRUTINIOS/2, Align.left, Align.center, 1, group); }
		else 
			{ printText(escrutinio.Categoria, PantallaResultados.font24, COLOR_CATEGORIA_BASE, COLOR_CATEGORIA_SOMBRA, 5, ALTURA_FILAS_ESCRUTINIOS/2, Align.left, Align.center, 1, group); }
		//Premio:
		printText(formatoPremio.format(premioNumber)+" €", PantallaResultados.font24, COLOR_RESULTADOS_BASE, COLOR_RESULTADOS_SOMBRA, 320, ALTURA_FILAS_ESCRUTINIOS/2, Align.right, Align.center, 1, group);
		//Acertantes:
		if (acertantesEsNumero)
			{ printText(formatoAcertantes.format(acertantesNumber), PantallaResultados.font24, COLOR_EXTRA_BASE, COLOR_EXTRA_SOMBRA, 475, ALTURA_FILAS_ESCRUTINIOS/2, Align.right, Align.center, 1, group); }
		else 
			{ printText(escrutinio.Acertantes, PantallaResultados.font24, COLOR_EXTRA_BASE, COLOR_EXTRA_SOMBRA, 475, ALTURA_FILAS_ESCRUTINIOS/2, Align.right, Align.center, 1, group); }
		
		Listeners.listenerCabeceraLineaEscrutinio(group);
		
		return group;
	}
	
	public static Group generarLineaEscrutinio ()
	{
		Group group = new Group();
		Image barra = Recursos.getBarraAzulClaro();
		group.addActor(barra);
		barra.setBounds(0, 0, PantallaResultados.anchoPantalla, ALTURA_FILAS_ESCRUTINIOS);
		
		printText("Escrutinio no encontrado.", PantallaResultados.font24, Color.WHITE, Color.BLACK, 5, 3, Align.left, Align.bottom, 1, group);
		
		Listeners.listenerCabeceraLineaEscrutinio(group);
		
		return group;
	}
	
	public static void generarBotes (Table tablaBotes)
	{	
		tablaBotes.clear();
		
		for (int i=0; i<CentroDeDatos.listaTiposSorteo.size; i++)
		{
			if (CentroDeDatos.listaTiposSorteo.get(i).getTieneBote())
			{ 
				tablaBotes.add(generarLineaBotesCabecera(CentroDeDatos.listaTiposSorteo.get(i))).left().height(ALTURA_FILAS_BOTES);
				tablaBotes.row();
				
				tablaBotes.add(generarLineaBotes(CentroDeDatos.listaTiposSorteo.get(i))).left().height(ALTURA_FILAS_BOTES); 
				tablaBotes.row(); 
			}
		}
		generarDisclaimer(tablaBotes);
	}
	
	public static Group generarLineaBotesCabecera (TiposSorteo sorteo)
	{
		Group group = new Group();
		
		//Barra de Fondo Sorteo
		Image barra = Recursos.getBarraAzulOscuro();
		group.addActor(barra);
		barra.setBounds(0, 0, PantallaResultados.anchoPantalla, ALTURA_FILAS_BOTES);
		
		Image iconoSorteo = Recursos.getLogo(sorteo.getNombre().toLowerCase());
		group.addActor(iconoSorteo);
		iconoSorteo.setPosition(5, 0);
		
		//Nombre Sorteo	
		if (!sorteo.getTieneBote() || !sorteo.getBoteEnActivo())
		{ 
			printText(sorteo.getNombreAPP(), PantallaResultados.fuenteCabecera, Color.WHITE, Color.BLACK, 55, 0, Align.left, Align.bottom, group);
			printText(sorteo.getDiaBote(), PantallaResultados.fuente24, COLOR_BOTESFECHA_BASE, COLOR_BOTESFECHA_SOMBRA, 475*0+PantallaResultados.anchoPantalla-10, 3, Align.right, Align.bottom, 2, group);
		}
		else
		{	
			printText(sorteo.getNombreAPP(), PantallaResultados.fuenteCabecera, Color.WHITE, Color.BLACK, 55, 0, Align.left, Align.bottom, group);
			printText(sorteo.getDiaBote(), PantallaResultados.fuente24, COLOR_BOTESFECHA_BASE, COLOR_BOTESFECHA_SOMBRA, 475*0+PantallaResultados.anchoPantalla-10, 3, Align.right, Align.bottom, 2, group);
		}
		return group;
	}
	
	public static Group generarLineaBotes (TiposSorteo sorteo)
	{
		Group group = new Group();
		
		Image barra = Recursos.getBarraAzulClaro();
		barra.setBounds(0, 0, PantallaResultados.anchoPantalla, ALTURA_FILAS_BOTES);
		group.addActor(barra);
					
		if (!sorteo.getTieneBote() || !sorteo.getBoteEnActivo())
		{ 
			printText("Sin Bote", PantallaResultados.font45, Color.LIGHT_GRAY, Color.DARK_GRAY, PantallaResultados.anchoPantalla-10, ALTURA_FILAS_BOTES/2+2, Align.right, Align.center, 3, group);
			//Bote.addAction(Actions.forever(Actions.sequence(Actions.alpha(0f, 5f, Interpolation.linear), Actions.fadeIn(0.5f))));
			printText(sorteo.getDiaDelMesBote()+"-"+sorteo.getMesBote()+"-"+sorteo.getAñoBote(), PantallaResultados.font24, Color.LIGHT_GRAY, Color.DARK_GRAY, 2, ALTURA_FILAS_BOTES/2+2, Align.left, Align.center, 2, group);
		}
		else
		{	
			printText(sorteo.getDiaDelMesBote()+"-"+sorteo.getMesBote()+"-"+sorteo.getAñoBote(), PantallaResultados.font24, COLOR_BOTESFECHA_BASE, COLOR_BOTESFECHA_SOMBRA, 2, ALTURA_FILAS_BOTES/2+2, Align.left, Align.center, 2, group);
			printText(sorteo.getResultadoBotes()[2]+" €", PantallaResultados.font45, COLOR_BOTES_BASE, COLOR_BOTES_SOMBRA, PantallaResultados.anchoPantalla-10, ALTURA_FILAS_BOTES/2+2, Align.right, Align.center, 3, group);
		}		
		return group;
	}
	
	public static void generarDisclaimer (Table tabla)
	{
		tabla.add(generarDisclaimer()).left().height(98);
		tabla.row();
	}
	
	public static Group generarDisclaimer ()
	{
		Group group = new Group();
		
		Image barra = Recursos.getBarraAzulOscuro();
		barra.setBounds(0, 0, PantallaResultados.anchoPantalla, 100);
		group.addActor(barra);
		
		printText("Loterías Reunidas ASG no se responsabiliza de la", PantallaResultados.font20,  Color.LIGHT_GRAY, Color.BLACK, PantallaResultados.anchoPantalla/2, 24+50, Align.center, Align.center, 1, group);
		printText("validez de los resultados, consulte las listas", PantallaResultados.font20,  Color.LIGHT_GRAY, Color.BLACK, PantallaResultados.anchoPantalla/2, 24+25, Align.center, Align.center, 1, group);
		printText("oficiales en su Punto de Venta SELAE.", PantallaResultados.font20,  Color.LIGHT_GRAY, Color.BLACK, PantallaResultados.anchoPantalla/2, 24, Align.center, Align.center, 1, group);
		
		return group;
	}
	
	public static void generarAlerta (String mensaje, boolean isBoletoEscaneado)
	{	
		
		final Group gAlerta = new Group();
		Image barra = Recursos.getBarraRoja();
		gAlerta.addActor(barra);
		barra.setBounds(0, 0, PantallaResultados.anchoPantalla, 49);	
		
		if (isBoletoEscaneado) { mensaje = reformatearStringScaneado (mensaje); }
				
		if (mensaje.length()<=30)
			{ printText(mensaje, PantallaResultados.fuente37, Color.ORANGE, Color.BLACK, PantallaResultados.anchoPantalla/2, 25, Align.center, Align.center, 3, gAlerta); }
		else if (mensaje.length()<= 48)
			{ printText(mensaje, PantallaResultados.font24, Color.ORANGE, Color.BLACK, PantallaResultados.anchoPantalla/2, 25, Align.center, Align.center, 3, gAlerta); }
		else
			{ printText(mensaje, PantallaResultados.font20, Color.ORANGE, Color.BLACK, PantallaResultados.anchoPantalla/2, 25, Align.center, Align.center, 3, gAlerta); }
				
		
		gAlerta.setPosition(0, PantallaResultados.altoPantalla+50);
	
		gAlerta.addAction(Actions.moveTo(0, PantallaResultados.altoPantalla/2, 0.6f, Interpolation.sine));
		gAlerta.addAction(Actions.sequence(Actions.alpha(1f, 8f), Actions.alpha(0f, 2f), Actions.removeActor(gAlerta)));
		
		((PantallaResultados)Loto.screen).stage.addActor(gAlerta);
		
		gAlerta.addListener(new ClickListener ()
		{
			 @Override
			 public void clicked (InputEvent event, float x, float y)
			 {	//((PantallaResultados)Loto.screen).stage.getRoot().removeActor(gAlerta); 
			 	gAlerta.addAction(Actions.sequence(Actions.alpha(0f, 0.6f), Actions.removeActor(gAlerta)));
			 }
		});
	}
	
	public static String reformatearStringScaneado (String mensaje)
	{
		mensaje = mensaje.replaceAll("\\n", "");
		mensaje = mensaje.trim();
		
		if (mensaje.equals("SORTEO_NO_VALIDO")) 			{ return "Este sorteo no existe"; }
		else if (mensaje.equals("SORTEO_CADUCADO")) 		{ return "Boleto caducado"; }
		else if (mensaje.equals("LISTAS_NO_DISPONIBLES"))	{ return "Listas todavia no disponibles"; }
		else if (mensaje.equals("0")) 						{ return "Boleto no premiado"; }
		else 
		{
			try { Integer.parseInt(mensaje); return "BOLETO PREMIADO, con "+mensaje+"€ por Décimo"; }
			catch (Exception e) { return "Error leyendo Boleto"; }
		}
	}
}











































/* Codigo antiguo, se queda por que esta la demostracion de como hacer que escrollee las lineas de resultados


Array<MenuBar> MenuResultados = new Array<MenuBar>(30);
	
	public static class MenuBar
	{
		Group group;
		int submenu=0;
	};
	
	public void addMenuResultados (Group group)
	{
		MenuBar menub = new MenuBar();
		menub.group = group;
		menub.submenu = 0;
		MenuResultados.add(menub);
	}


public Group GenerarCabeceraSorteo (String TipoSorteo, int numResultados, String TextoResultados, String [] ExtraDesc)
{
	Group group = new Group();
	
	//Nombre Sorteo
	printText (TipoSorteo, fuenteCabecera, Color.WHITE, Color.BLACK, 45, 0, group);
	//Imagen tipo Sorteo
	Image logoSorteo = new Image(atlas.findRegion("bonoloto"));
	group.addActor(logoSorteo);
	logoSorteo.setBounds(0, 0, 45, 45);
	//Barra de Fondo Sorteo
	Image barra = new Image(atlas.findRegion("barra_titulo"));
	group.addActor(barra);
	barra.toBack();
	barra.setBounds(0, 0, anchoPantalla, 39);
	//Barra de Fondo Descripcion Campos
	Image barraDescripcion = new Image(atlas.findRegion("barra"));
	group.addActor(barraDescripcion);
	barraDescripcion.setBounds(-anchoPantalla, 0, anchoPantalla, 39);
	barraDescripcion.toBack();
	//Dia/Fecha Descripcion Campos
	printText ("Dia / Fecha", fuenteDescCampos, Color.RED, Color.BLACK, -anchoPantalla+5, 3, group);
	printText (TextoResultados, fuenteDescCampos, Color.RED, Color.BLACK, -anchoPantalla+150+40*(numResultados)/2, 3, Align.center, group);
	for (int i=0; i<ExtraDesc.length; i++)
	{
		printText(ExtraDesc[i], fuenteMiniDescCampos, Color.RED, Color.BLACK, -anchoPantalla+150+40*numResultados+40*(i+1), 3, Align.right, group);
	}
	
	//Click que activa el scroll hacia la descripcion de los campos, hay que cambiarlo a un icono en un extremo, toda la barra se hace incomodo
	group.addListener(new InputListener()
	{
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
        {
			
			
			Group g = event.getTarget().getParent();
			
            if (g.getX()==0) { g.addAction( Actions.moveTo(anchoPantalla, g.getY(), 1f, Interpolation.swing)    ); }
            else {event.getTarget().getParent().addAction( Actions.sequence(  Actions.moveTo(0, g.getY(), 1f, Interpolation.sine)    )); }
            return true;
        }
	});
	return group;
}

/*
		//BONOLOTO
		tablaSorteo = new Table().top().left();
		tablaSorteo.setClip(false);
		String [] ExtraDesc = new String [2];
		ExtraDesc [0] = "Comp";
		ExtraDesc [1] = "Reint";
		Array <Group> grupoBonoloto = new Array<Group>();
		//tablaSorteo.add(GenerarCabeceraSorteo("Bonoloto", 6, "Resultados", ExtraDesc)).left().height(39);
		Group grupo = GenerarCabeceraSorteo2("Bonoloto", 6, "Resultados", ExtraDesc, 4);
		addMenuResultados (grupo);
		tablaSorteo.add(grupo).left().height(39);
		tablaSorteo.row();
		for (int i=0; i<CentroDeDatos.bonolotoSemana.size;i++)
		{
			String [] Resultados = new String [6];
			String [] Extra = new String [2];
			for (int j=0; j<6; j++) { Resultados[j] = CentroDeDatos.bonolotoSemana.get(i).getResultado()[j];	}
			Extra[0] = CentroDeDatos.bonolotoSemana.get(i).getResultado()[6];
			Extra[1] = CentroDeDatos.bonolotoSemana.get(i).getResultado()[7];
			grupo = GenerarLineaSorteo(CentroDeDatos.bonolotoSemana.get(i).getDia(),CentroDeDatos.bonolotoSemana.get(i).getFecha(), Resultados, Extra);
			addMenuResultados (grupo);
			grupoBonoloto.add(grupo);
			tablaSorteo.add(grupo).left().height(39);
			tablaSorteo.row();
		}
		tablaResultadosSemana.add(tablaSorteo);
		tablaResultadosSemana.row();
*/
/*
public Group GenerarCabeceraSorteo2 (String TipoSorteo, int numResultados, String TextoResultados, String [] ExtraDesc, final int numDias)
{
	Group group = new Group();
	
	//Nombre Sorteo
	printText (TipoSorteo, fuenteCabecera, Color.WHITE, Color.BLACK, 5, 0, group);
	//Barra de Fondo Sorteo
	Image barra = new Image(atlas.findRegion("barra_titulo"));
	group.addActor(barra);
	barra.toBack();
	barra.setBounds(0, 0, anchoPantalla, 39);
	//Barra de Fondo Descripcion Campos
	Image barraDescripcion = new Image(atlas.findRegion("barra"));
	group.addActor(barraDescripcion);
	barraDescripcion.setBounds(-anchoPantalla, 0, anchoPantalla, 39);
	barraDescripcion.toBack();
	//Dia/Fecha Descripcion Campos
	printText ("Dia / Fecha", fuenteDescCampos, Color.RED, Color.BLACK, -anchoPantalla+5, 3, group);
	printText (TextoResultados, fuenteDescCampos, Color.RED, Color.BLACK, -anchoPantalla+150+40*(numResultados)/2, 3, Align.center, group);
	for (int i=0; i<ExtraDesc.length; i++)
	{
		printText(ExtraDesc[i], fuenteMiniDescCampos, Color.RED, Color.BLACK, -anchoPantalla+150+40*numResultados+40*(i+1), 3, Align.right, group);
	}
	
	final int pos = MenuResultados.size;
	
	//Click que activa el scroll hacia la descripcion de los campos, hay que cambiarlo a un icono en un extremo, toda la barra se hace incomodo
	group.addListener(new InputListener()
	{
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) 
        {
			for (int i=1; i<=numDias;i++)
			{
				if (MenuResultados.get(pos+i).group.getX()==0) { MenuResultados.get(pos+i).group.addAction( Actions.moveTo(anchoPantalla,  MenuResultados.get(pos+i).group.getY(), 1f, Interpolation.sine)    ); }
                else { MenuResultados.get(pos+i).group.addAction( Actions.sequence(  Actions.moveTo(0,  MenuResultados.get(pos+i).group.getY(), 1f, Interpolation.sine)    )); }
			}
			
            return true;
        }
	});
	return group;
}

public Group GenerarLineaSorteo(String Dia, Date Fecha, String [] Resultados, String [] Extra)
{	
	Group group = new Group();
	
	//Dia
	Label labelDia = printText (Dia, fuenteResultados, Color.ORANGE, Color.BLACK, 5, 3, group);
	//Fecha
	if (Dia != "") 
	{
		String fecha = Integer.toString(Fecha.getDate())+"/"+Integer.toString(Fecha.getMonth());
		printText (fecha, fuenteFecha, Color.WHITE, Color.BLACK, (int)labelDia.getWidth()+10, 3, group);
	}
	//Resultados
	for (int i=0; i<Resultados.length; i++)
	{ printText (Resultados[i], fuenteResultados, Color.WHITE, Color.BLACK, 185+i*40, 3, Align.right, group);}
	//Resultados Extra (Complementario, Reintegro, Estrellas...
	for (int i=0; i<Extra.length;i++)
	{ printText (Extra[i], fuenteResultados, Color.ORANGE, Color.BLACK, 185+Resultados.length*40+i*40+5, 3, Align.right, group); }
	//Barra de Fondo
	Image barra = new Image(atlas.findRegion("barra"));
	group.addActor(barra);
	barra.toBack(); 
	barra.setBounds(0, 0, anchoPantalla, 39);
	
	return group;
}
*/