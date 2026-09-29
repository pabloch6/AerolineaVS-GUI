package com.aerolineavs.tarifas;

/**
 * Business rules for selecting the most suitable fare.
 */
public final class PricingServiceImpl implements IPricingService {

    private static final String SUPOSICIONES = "Suposiciones: curso universitario = 9 desplazamientos/año; "
            + "la frecuencia anual informada representa los viajes relevantes de cada regla; "
            + "si no se cumple ninguna condición exacta, no se oferta tarifa.";

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultadoTarifa evaluar(ClientePotencial cliente) {
        if (cliente.edad() < 18
                && cliente.tipoViajero() == TipoViajero.MENOR
                && cliente.vuelosAnuales() >= 6) {
            return new ResultadoTarifa(Tarifa.PAJARILLO, SUPOSICIONES);
        }

        if (cliente.edad() >= 18 && cliente.edad() <= 25) {
            if (cliente.tipoViajero() == TipoViajero.ESTUDIANTE_UNIVERSITARIO_DESPLAZADO
                    && cliente.clasePreferida() == ClaseVuelo.TURISTA
                    && cliente.vuelosAnuales() >= 9) {
                return new ResultadoTarifa(Tarifa.GORRION, SUPOSICIONES);
            }

            if (cliente.tipoViajero() == TipoViajero.TRABAJADOR_JOVEN
                    && cliente.clasePreferida() == ClaseVuelo.TURISTA
                    && cliente.vuelosAnuales() >= 3) {
                Tarifa tarifa = cliente.viveConPadres()
                        ? Tarifa.VIAJA_AHORA_QUE_PUEDES
                        : Tarifa.ATREVIENDOSE_A_SALTAR_DEL_NIDO;
                return new ResultadoTarifa(tarifa, SUPOSICIONES);
            }
        }

        if (cliente.edad() > 25) {
            if (cliente.ingresosAnuales() > 20000
                    && cliente.ingresosAnuales() < 35000
                    && cliente.vuelosAnuales() >= 6
                    && cliente.clasePreferida() == ClaseVuelo.TURISTA
                    && cliente.destinoPreferido() == RegionDestino.EUROPA) {
                Tarifa tarifa = cliente.viajaConNinos()
                        ? Tarifa.CONOCE_EUROPA_CON_TUS_PEQUES
                        : Tarifa.CONOCE_EUROPA;
                return new ResultadoTarifa(tarifa, SUPOSICIONES);
            }

            if (cliente.ingresosAnuales() > 35000
                    && cliente.vuelosAnuales() >= 6
                    && cliente.clasePreferida() == ClaseVuelo.BUSINESS
                    && (cliente.destinoPreferido() == RegionDestino.ASIA
                    || cliente.destinoPreferido() == RegionDestino.AMERICA)) {
                Tarifa tarifa = cliente.viajaConNinos()
                        ? Tarifa.CONOCE_EL_MUNDO_CON_TUS_PEQUES
                        : Tarifa.CONOCE_EL_MUNDO;
                return new ResultadoTarifa(tarifa, SUPOSICIONES);
            }
        }

        return new ResultadoTarifa(Tarifa.SIN_TARIFA, SUPOSICIONES);
    }
}