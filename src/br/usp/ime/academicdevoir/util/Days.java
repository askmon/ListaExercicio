package br.usp.ime.academicdevoir.util;

import java.util.Calendar;
import java.util.Date;

public class Days {
	
	private static final int tempoDia = 1000 * 60 * 60 * 24;
	
	public static long daysBetween(Date inicio) {
    Calendar dataInicio = Calendar.getInstance();
    dataInicio.setTime(inicio);
    
    Calendar dataFinal = Calendar.getInstance();
    dataFinal.setTime(new Date());
    
    long diferenca = dataFinal.getTimeInMillis() -
                     dataInicio.getTimeInMillis();
   
    long diasDiferenca = diferenca / tempoDia;
   
		return diasDiferenca;
	}
	
}
