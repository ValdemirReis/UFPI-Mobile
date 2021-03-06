package ufpi.br.ufpimobile.model;

/**
 * Created by UFPI 236345 on 13/12/2017.
 */

public class Noticia {


    /**
     * code : 21842
     * createdAt : 2018-02-05T17:59:00.000Z
     * titulo : PPGMAT: retificação do edital de seleção
     * href : http://ufpi.br/ultimas-noticias-ufpi/21842-ppgmat-retificacao-do-edital-de-selecao
     * _data : 05/02/2018
     * hora : 14:59
     */

    private int code;
    private String createdAt;
    private String titulo;
    private String href;
    private String _data;
    private String hora;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String get_data() {
        return _data;
    }

    public void set_data(String _data) {
        this._data = _data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
