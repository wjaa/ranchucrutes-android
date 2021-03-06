package br.com.agendee.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.agendee.activity.DetalhesConsultaActivity;
import br.com.agendee.service.RanchucrutesConstants;
import br.com.agendee.utils.AndroidUtils;
import br.com.agendee.utils.DateUtils;
import br.com.agendee.R;
import br.com.agendee.vo.AgendamentoVo;

/**
 * Created by wagner on 05/11/15.
 */
public class MinhasConsultasListAdapter extends ArrayAdapter<AgendamentoVo> {

    private AgendamentoVo[] objects;
    private Context context;

    public MinhasConsultasListAdapter(Context context, AgendamentoVo[] objects) {
        super(context, -1, objects);
        this.objects = objects;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AgendamentoVo agendamentoVo = objects[position];
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.minhas_consultas_item_list, parent, false);

        ImageView icStatus = (ImageView) rowView.findViewById(R.id.icStatus);
        String statusConsulta = "Você não confirmou essa solicitação.";
        int colorStatus = R.color.warningTextColor;
        if (agendamentoVo.getCancelado()){
            icStatus.setImageResource(R.drawable.ic_cancel);
            statusConsulta = "Essa consulta foi cancelada!";
            colorStatus = R.color.errorTextColor;
        }else{
            if (agendamentoVo.getDataConfirmacao() != null){
                icStatus.setImageResource(R.drawable.ic_warning);
                statusConsulta = "Essa consulta está aguardando a confirmação do profissional.";
                colorStatus = R.color.warningTextColor;
            }
            if (agendamentoVo.getFinalizado()){
                icStatus.setImageResource(R.drawable.ic_success);
                statusConsulta = "Consulta realizada!";
                colorStatus = R.color.successTextColor;
            }else if (agendamentoVo.getDataConfirmacaoConsulta() != null){
                icStatus.setImageResource(R.drawable.ic_success);
                statusConsulta = "Consulta confirmada!";
                colorStatus = R.color.successTextColor;
            }else if (agendamentoVo.getDataConfirmacaoProfissional() != null){
                icStatus.setImageResource(R.drawable.ic_success);
                statusConsulta = "Solicitação confirmada pelo profissional.";
                colorStatus = R.color.successTextColor;
            }
        }

        TextView especialidadeData = (TextView) rowView.findViewById(R.id.lbEspecialidadeData);
        especialidadeData.setText(agendamentoVo.getProfissional().getEspec() + " em " +
                DateUtils.formatddMMyyyyHHmm(agendamentoVo.getDataAgendamento()));

        TextView lbNomeMedico = (TextView) rowView.findViewById(R.id.lbNomeMedico);
        lbNomeMedico.setText(agendamentoVo.getProfissional().getNome());

        TextView lbEndereco = (TextView) rowView.findViewById(R.id.lbEndereco);
        lbEndereco.setText(agendamentoVo.getProfissional().getEndereco());

        TextView lbStatus = (TextView) rowView.findViewById(R.id.lbDetalhes);
        lbStatus.setText(statusConsulta);
        lbStatus.setTextColor(context.getResources().getColor(colorStatus));


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable(RanchucrutesConstants.PARAM_AGENDAMENTO,agendamentoVo);
                AndroidUtils.openActivity((Activity)context, DetalhesConsultaActivity.class, b);
            }
        });

        return rowView;
    }
}
