package br.com.wjaa.ranchucrutes.vo;

import java.io.Serializable;

import br.com.wjaa.ranchucrutes.view.SearchableListModel;

public class EspecialidadeVo implements Serializable, SearchableListModel{


    private static final long serialVersionUID = -1602319229739751782L;
    private Integer id;
    private String nome;
    private Boolean popular;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    @Override
    public String getName() {
        return this.nome;
    }
}
