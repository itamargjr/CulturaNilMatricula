package manager;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import entity.Cult_matr_candidato;
import entity.Cult_segmento;
import entity.Edu_matr_responsavel;
import entity.End_endereco;
import persistence.Cult_matr_candidatoDao;
import persistence.Cult_segmentoDao;
import persistence.Edu_escolaDao;
import persistence.Edu_matr_candidatoReportDao;
import persistence.Edu_matr_responsavelDao;
import persistence.End_enderecoDao;
import util.Biblioteca;

@ManagedBean
@ViewScoped
public class Cult_matr_candidatoBean {
	
	public Cult_matr_candidato candidato;
	public Edu_matr_responsavel responsavel;
	
	public End_endereco endereco;
	
	public Cult_segmento segmento;
	
	public List<Cult_matr_candidato> candidatolista;
	public List<Cult_segmento> segmentolista;
	public List<Cult_segmento> segmentoselecionadolista;
	
	public Integer idade;
	
	public Boolean necespec;
	
	public Cult_matr_candidatoBean() {
		
		limpaformulario();
		
	}
	
	public Boolean getNecespec() {
		return necespec;
	}

	public void setNecespec(Boolean necespec) {
		this.necespec = necespec;
	}

	public List<Cult_segmento> getSegmentoselecionadolista() {
		return segmentoselecionadolista;
	}

	public void setSegmentoselecionadolista(List<Cult_segmento> segmentoselecionadolista) {
		this.segmentoselecionadolista = segmentoselecionadolista;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Cult_matr_candidato getCandidato() {
		return candidato;
	}

	public void setCandidato(Cult_matr_candidato candidato) {
		this.candidato = candidato;
	}

	public Edu_matr_responsavel getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Edu_matr_responsavel responsavel) {
		this.responsavel = responsavel;
	}

	public End_endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(End_endereco endereco) {
		this.endereco = endereco;
	}

	public Cult_segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Cult_segmento segmento) {
		this.segmento = segmento;
	}

	public List<Cult_matr_candidato> getCandidatolista() {
		return candidatolista;
	}

	public void setCandidatolista(List<Cult_matr_candidato> candidatolista) {
		this.candidatolista = candidatolista;
	}

	public List<Cult_segmento> getSegmentolista() {
		return segmentolista;
	}

	public void setSegmentolista(List<Cult_segmento> segmentolista) {
		this.segmentolista = segmentolista;
	}
	
	private void limpaformulario() {
		candidato = new Cult_matr_candidato();
		segmento = new Cult_segmento();
		responsavel = new Edu_matr_responsavel();
		endereco = new End_endereco();
		
		segmentoselecionadolista = new ArrayList<Cult_segmento>();
		segmentolista = new ArrayList<Cult_segmento>();
		candidatolista = new ArrayList<Cult_matr_candidato>();
		
		idade = 0;
		
		necespec = false;
	}

	public void calculaidade() {
		if (!candidato.getNascimento_candidato().equalsIgnoreCase("")) {
			
			idade = Biblioteca.retornaIdade(candidato.getNascimento_candidato());//, "31/03/2022").toString();
			
			candidato.setTipo_candidato("C"); // Setando tipo para candidato da cultura
			
			preenchelistas();
			
		}
	}
	
	private void preenchelistas() {

		if (idade < 4) {
					
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage("Idade inválida!",  "O  candidato deverá ter a partir de 4 anos") );				
		
		} else {
			
			try {
				
				segmento.setIdadebusca(idade);
				
				segmentolista = new Cult_segmentoDao().findAll(segmento);
				
				segmentoselecionadolista = new ArrayList<Cult_segmento>();
				
				//System.out.println("segmentolista: " + segmentolista);
				
			} catch (Exception e) {
				e.printStackTrace();
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			}					
			
		}
		
	}
	
	public void ConcluirInscricao() {
		
		candidato.setAno_candidato(2022);
		candidato.setTipo_candidato("C");
		
		try {						
			
			Cult_matr_candidatoDao cd = new Cult_matr_candidatoDao();
			
			Boolean Candidatojaexiste = cd.testaCandidatoJaExiste(candidato);
			
			if (Candidatojaexiste) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Este candidato já possui inscrição", "")); // passa a mensagem
			} else if (segmentoselecionadolista.size()==0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Alguma opção de escola deve ser escolhida", "")); // passa a mensagem
			} else if (segmentoselecionadolista.size()>5) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Por favor, selecione no máximo 5 (CINCO) opções de segmentos", "")); // passa a mensagem
			} else {
				
				for (int i = 0; i < segmentoselecionadolista.size(); i++) {
					if (i==0) {
						candidato.setId_segmento1(segmentoselecionadolista.get(i).getId_segmento());
					} else if (i==1) {
						candidato.setId_segmento2(segmentoselecionadolista.get(i).getId_segmento());
					} else if (i==2) {
						candidato.setId_segmento3(segmentoselecionadolista.get(i).getId_segmento());
					} else if (i==3) {
						candidato.setId_segmento4(segmentoselecionadolista.get(i).getId_segmento());
					} else  {
						candidato.setId_segmento5(segmentoselecionadolista.get(i).getId_segmento());
					} 								
				}
				
				if (necespec) {
					candidato.setNecespec_candidato("S");
				} else {
					candidato.setNecespec_candidato("N");
				}
				
				Edu_matr_responsavelDao rd = new Edu_matr_responsavelDao();
				
				Integer idresp = rd.gravar(responsavel);
				
				candidato.setId_responsavel(idresp);	
				
				System.out.println("candidato: " + candidato);
				
				Integer idcandidato = cd.gravar(candidato);
				
				endereco.setIdentidade_endereco(idcandidato);
				endereco.setTipo_endereco("U");
				endereco.setUf_endereco("RJ");
				endereco.setCodigo_municipio("3303203");
				
				End_enderecoDao ed = new End_enderecoDao();
				
				ed.gravar(endereco);
							
//				enviaremailcandidato(idcandidato);
				
				if (idcandidato == 0) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Houve um erro na gravação do candidato", "")); // passa a mensagem
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Candidato gravado com sucesso", "")); // passa a mensagem
					
					limpaformulario();				
		
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "")); // passa a mensagem
		}
		
	}
	
	public void buscarcandidato() {
		try {
			
			//System.out.println("Buscando candidato: " + candidatoreport);
			
			candidato = new Cult_matr_candidatoDao().findCandidatoConsulta(candidato);
			
			if (candidato.getId_segmento1()==null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Candidato não encontrado com os dados fornecidos", "")); // passa a mensagem
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "")); 	
		}
	}
	
	public void imprimirInscricao(){
		
	}
	
	public void reenviarInscricao() {
		
	}
}
