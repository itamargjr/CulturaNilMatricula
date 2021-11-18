package manager;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import entity.Cult_matr_candidato;
import entity.Cult_segmento;
import entity.Edu_matr_responsavel;
import entity.End_endereco;
import net.sf.jasperreports.engine.JasperRunManager;
import persistence.Cult_matr_candidatoDao;
import persistence.Cult_segmentoDao;
import persistence.Edu_matr_responsavelDao; 
import persistence.End_enderecoDao;
import report.DSReportCandidato;
import util.Biblioteca;
import util.SendMail;

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

			context.addMessage(null, new FacesMessage("Idade inv�lida!",  "O  candidato dever� ter a partir de 4 anos") );				
		
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
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Este candidato j� possui inscri��o", "")); // passa a mensagem
			} else if (segmentoselecionadolista.size()==0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Alguma op��o de escola deve ser escolhida", "")); // passa a mensagem
			} else if (segmentoselecionadolista.size()>5) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Por favor, selecione no m�ximo 5 (CINCO) op��es de segmentos", "")); // passa a mensagem
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
							
				enviaremailcandidato(idcandidato);
				
				if (idcandidato == 0) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Houve um erro na grava��o do candidato", "")); // passa a mensagem
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
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Candidato n�o encontrado com os dados fornecidos", "")); // passa a mensagem
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "")); 	
		}
	}
	
	public void imprimirInscricao(){
		imprimeCandidato(candidato.getId_candidato());
	}
	
	public void reenviarInscricao() {
		try {
			enviaremailcandidato(candidato.getId_candidato());
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Email enviado com sucesso", ""));
		} catch (Exception e) {
			e.printStackTrace();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
		}
	}
	
	public void cancelarInscricao() {
		try {
			Cult_matr_candidatoDao cd = new Cult_matr_candidatoDao();
			
			cd.cancelaInscricao(candidato);
			
			candidato = new Cult_matr_candidato();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Inscri��o cancelada!", "")); 
			
		} catch (Exception e) {
			e.printStackTrace();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "")); 
		}
	}
	

	
	public void enviaremailcandidato(Integer IdCandidato){
		
		if ((IdCandidato==null)||(IdCandidato==0)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum candidato encontrado para enviar email", "")); // passa a mensagem
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			try{		
				
				List<Cult_matr_candidato> lista = new Cult_matr_candidatoDao().findCandidato(IdCandidato);
				
				candidato = lista.get(0);
				
				System.out.println("candidato: " +candidato);

				DSReportCandidato ds = new DSReportCandidato(lista);
				
				InputStream arquivo = FacesContext.getCurrentInstance()
					.getExternalContext().getResourceAsStream("/candidatoCultura.jasper");	

				byte[] pdf = JasperRunManager.runReportToPdf(arquivo, null, ds);
					
				String dest = "";
				
				if (candidato.getEmail_candidato()!=null) {
					dest = candidato.getEmail_candidato();
				} else if (candidato.getEmail_responsavel()!=null) {
					dest = candidato.getEmail_responsavel();
				}
				
				SendMail sm = new SendMail();
				
				String dadosemailhtml = "<br /> <strong>SECRETARIA DE CULTURA NIL�POLIS 2022 - INSCRI��O CANDIDATO <label>" +
		           		 "</label> </strong><br /> <br />" +
		           		 "<br /> Esta � uma confirma��o da sua CANDIDATURA aos Segmentos na Secretaria de Cultura do munic�pio de Nil�polis <br />" +
		           		 "<hr />" +
		           		 "<br /><br />Candidato         : " + candidato.getNome_candidato() +
		           		 "<br /><br />Data de Nascimento: " + candidato.getNascimento_candidato() +
		           		 "<br /><br />Bairro            : " + candidato.getBairro_candidato() +
		           		 "<br /><br />" + 
		           		 "<br /><br />Respons�vel       : " + candidato.getNome_responsavel() +
		           		 "<br /><br />" + 		           		 
		           		 "<br /><br />Op��es de Escola  : " + 
		           		 "<br /><br />" + 
		           		 "<br /><br />1� Op��o          : " + candidato.getDescricao_segmento1() +		           		 		           		
		           		 "<br /><br />2� Op��o          : " + candidato.getDescricao_segmento2() +
		           		 "<br /><br />3� Op��o          : " + candidato.getDescricao_segmento3() +
		           		 "<br /><br />4� Op��o          : " + candidato.getDescricao_segmento4() +
		           		 "<br /><br />5� Op��o          : " + candidato.getDescricao_segmento5() +
		           		 "<br /><br />" + 
		           		 "Voc� realizou a inscri��o para concorrer a vagas na " +
		           		 "<br />" +
		           		 "Secretaria de Cultura do Munic�pio de Nil�polis em 2022. O resultado final  (Classifica��o) estar� " +
		           		 "<br />" +
		           		 "dispon�vel no mesmo endere�o eletr�nico." +
		           		 "<br />" +
		           		 "(www.nilopolisdigital.com/matriculasonline)"+
		           		 "<div style='border:none;border-bottom:solid windowtext 1.0pt;padding:0cm 0cm 1.0pt 0cm'>" +
		           		 
		           		 "<table align='center'><tr>"+
		           		 "<td align='center'><img src='http://nilopolisdigital.com/imagens/cultura.jpg' alt='Matr�culas On Line 2022' /><br /><br />"+
		           		 "<strong>Sistema de Matr�culas On Line 2022 - Prefeitura Municipal de Nil�polis</strong>"+
		           		 "</td></tr></table>";
	
				
				String[] to = {dest, candidato.getEmail_responsavel()}; 
				
				//System.out.println("para: " + to);
							
				sm.sendMailAttachment("inscricaoeducacaonilopolis@gmail.com", to, "Matr�cula Nil�polis 2022 - Candidato", dadosemailhtml, pdf);

				//fc.addMessage("enviaremailprepedidos", new FacesMessage("Email enviado com sucesso! "));	
				
			} catch (Exception e) {
				e.printStackTrace();
				
				fc.addMessage("enviaremailprepedidos", new FacesMessage("Erro ao enviar: " + e.getMessage()));	
			}
		}
	}
	
	public String imprimeCandidato(Integer IdCandidato){
		
		if ((IdCandidato==null)||(IdCandidato==0)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum candidato encontrado para enviar email", "")); // passa a mensagem
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			try{		
				
				List<Cult_matr_candidato> lista = new Cult_matr_candidatoDao().findCandidato(IdCandidato);
				
				candidato = lista.get(0);

				DSReportCandidato ds = new DSReportCandidato(lista);
				
				InputStream arquivo = FacesContext.getCurrentInstance()
					.getExternalContext().getResourceAsStream("/candidatoCultura.jasper");	

				byte[] pdf = JasperRunManager.runReportToPdf(arquivo, null, ds);
					
				HttpServletResponse res = (HttpServletResponse) FacesContext
						.getCurrentInstance().getExternalContext().getResponse();
					
				res.setContentType("application/pdf");
				
				res.setContentLength(pdf.length);
					
				OutputStream out = res.getOutputStream();

				out.write(pdf, 0, pdf.length);

				out.flush();	
				
				out.close();
				
				FacesContext.getCurrentInstance().responseComplete();
				
				OutputStream fileout = new FileOutputStream("relatoriopreped.pdf");	
				
				fileout.write(pdf, 0, pdf.length);

				fileout.flush();	

				fileout.close();	
				
			} catch (Exception e) {
				e.printStackTrace();
				
				fc.addMessage("enviaremailprepedidos", new FacesMessage("Erro ao enviar: " + e.getMessage()));	
			}
		}
		
		return null;
	}
}
