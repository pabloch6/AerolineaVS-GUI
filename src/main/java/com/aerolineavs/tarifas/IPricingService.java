package com.aerolineavs.tarifas;

/**
 * Contract for evaluating the most suitable fare for a potential customer.
 */
public interface IPricingService {

    /**
     * Evaluates one applicable fare from the customer's details.
     *
     * @param cliente customer data
     * @return the resulting fare and the assumptions used
     */
    ResultadoTarifa evaluar(ClientePotencial cliente);
}