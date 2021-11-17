package persistence;

import java.util.ArrayList;
import java.util.List;

import entity.Cult_segmento;

public class Cult_segmentoDao extends Dao {
	
	public List<Cult_segmento> findAll(Cult_segmento segmento) throws Exception{
		
		List<Cult_segmento> lista = new ArrayList<Cult_segmento>();
		
		open();
		
		String statement = "select id_segmento, ano_segmento, descricao_segmento, " +
						   "idademin_segmento, idademax_segmento, dia_segmento, " +
						   "turno_segmento, vagas_segmento " +
						   "from cult_segmento where 1 = 1 ";
		
		if ((segmento.getAno_segmento()!=null)&&(segmento.getAno_segmento()!=0)) {
			statement = statement + " and ano_segmento = " + segmento.getAno_segmento();
		}
		
		if ((segmento.getIdadebusca()!=null)&&(segmento.getIdadebusca()!=0)) {
			statement = statement + " and idademin_segmento <= " + segmento.getIdadebusca() + 
									" and idademax_segmento >= " + segmento.getIdadebusca();
		}
		
		if ((segmento.getDia_segmento()!=null)&&(!segmento.getDia_segmento().equalsIgnoreCase(""))) {
			statement = statement + " and dia_segmento = '" + segmento.getDia_segmento() + "'";
		}
		
		if ((segmento.getTurno_segmento()!=null)&&(!segmento.getTurno_segmento().equalsIgnoreCase(""))) {
			statement = statement + " and turno_segmento = '" + segmento.getTurno_segmento() + "'";
		}
		
		stmt = con.prepareStatement(statement);
		
		rs = stmt.executeQuery();
		
		while (rs.next()) {
			Cult_segmento c = new Cult_segmento(rs.getInt("id_segmento"), rs.getInt("ano_segmento"), rs.getString("descricao_segmento"),
					                      rs.getInt("idademin_segmento"), rs.getInt("idademax_segmento"), rs.getString("dia_segmento"),
					                      rs.getString("turno_segmento"), rs.getInt("vagas_segmento"));
			lista.add(c);
		}
		
		close();
		
		return lista;
		
	}

}
