package com.aerolineavs.tarifas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 * Swing interface for requesting a fare recommendation.
 */
public final class PricingGui extends JFrame {

    private static final Color INK = new Color(30, 48, 51);
    private static final Color TEAL = new Color(0, 112, 111);
    private static final Color CORAL = new Color(207, 83, 55);
    private static final Color CANVAS = new Color(241, 245, 242);
    private static final Color MUTED = new Color(94, 111, 109);

    private final IPricingService pricingService;
    private final JSpinner ageField = new JSpinner(new SpinnerNumberModel(30, 0, 120, 1));
    private final JSpinner flightsField = new JSpinner(new SpinnerNumberModel(6, 0, 100, 1));
    private final JTextField incomeField = new JTextField("25000", 12);
    private final JComboBox<Option<TipoViajero>> travelerTypeField = options(
            TipoViajero.values(), PricingGui::travelerLabel
    );
    private final JComboBox<Option<ClaseVuelo>> flightClassField = options(
            ClaseVuelo.values(), PricingGui::flightClassLabel
    );
    private final JComboBox<Option<RegionDestino>> destinationField = options(
            RegionDestino.values(), PricingGui::destinationLabel
    );
    private final JCheckBox childrenField = new JCheckBox("Viaja con niños menores de 12");
    private final JCheckBox parentsField = new JCheckBox("Vive con sus padres");
    private final JLabel fareName = new JLabel("Completa el perfil");
    private final JLabel discount = new JLabel(" ");
    private final JTextArea assumptions = new JTextArea(4, 24);
    private final JLabel status = new JLabel(" ");

    public PricingGui(IPricingService pricingService) {
        super("Aerolínea | Tarifas");
        this.pricingService = pricingService;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(760, 560));
        setSize(920, 650);
        setLocationByPlatform(true);
        getContentPane().setBackground(CANVAS);
        setLayout(new BorderLayout(0, 0));
        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PricingGui(new PricingServiceImpl()).setVisible(true));
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(INK);
        header.setBorder(BorderFactory.createEmptyBorder(22, 30, 22, 30));

        JLabel brand = new JLabel("AEROLÍNEA  /  TARIFAS");
        brand.setForeground(new Color(143, 207, 194));
        brand.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel title = new JLabel("Encuentra tu tarifa");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JPanel titles = new JPanel(new GridBagLayout());
        titles.setOpaque(false);
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.anchor = GridBagConstraints.WEST;
        titleConstraints.gridy = 0;
        titles.add(brand, titleConstraints);
        titleConstraints.gridy = 1;
        titleConstraints.insets = new Insets(5, 0, 0, 0);
        titles.add(title, titleConstraints);
        header.add(titles, BorderLayout.WEST);
        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.insets = new Insets(0, 0, 0, 14);
        constraints.gridx = 0;
        constraints.weightx = 0.58;
        content.add(createForm(), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.42;
        constraints.insets = new Insets(0, 0, 0, 0);
        content.add(createResultPanel(), constraints);
        return content;
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 226, 221)),
                BorderFactory.createEmptyBorder(20, 20, 18, 20)
        ));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = new Insets(0, 0, 14, 0);
        JLabel sectionTitle = new JLabel("Perfil del viajero");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(INK);
        form.add(sectionTitle, constraints);

        constraints.gridwidth = 1;
        addField(form, constraints, "Edad", ageField);
        addField(form, constraints, "Vuelos al año", flightsField);
        addField(form, constraints, "Ingresos anuales (€)", incomeField);
        addField(form, constraints, "Tipo de viajero", travelerTypeField);
        addField(form, constraints, "Clase preferida", flightClassField);
        addField(form, constraints, "Destino preferido", destinationField);
        addField(form, constraints, "", childrenField);
        addField(form, constraints, "", parentsField);

        JButton calculate = new JButton("Calcular tarifa");
        calculate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calculate.setForeground(Color.WHITE);
        calculate.setBackground(TEAL);
        calculate.setFocusPainted(false);
        calculate.setBorderPainted(false);
        calculate.setOpaque(true);
        calculate.addActionListener(this::calculateFare);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(12, 0, 0, 0);
        form.add(calculate, constraints);

        status.setForeground(CORAL);
        status.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        constraints.gridy++;
        constraints.insets = new Insets(8, 0, 0, 0);
        form.add(status, constraints);
        return form;
    }

    private JPanel createResultPanel() {
        JPanel result = new JPanel(new GridBagLayout());
        result.setBackground(new Color(225, 238, 230));
        result.setBorder(BorderFactory.createEmptyBorder(24, 22, 22, 22));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.insets = new Insets(0, 0, 18, 0);

        JLabel eyebrow = new JLabel("RESULTADO");
        eyebrow.setForeground(TEAL);
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 12));
        result.add(eyebrow, constraints);

        fareName.setForeground(INK);
        fareName.setFont(new Font("Segoe UI", Font.BOLD, 25));
        fareName.setVerticalAlignment(JLabel.TOP);
        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 8, 0);
        result.add(fareName, constraints);

        discount.setForeground(CORAL);
        discount.setFont(new Font("Segoe UI", Font.BOLD, 21));
        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 22, 0);
        result.add(discount, constraints);

        JSeparatorLine separator = new JSeparatorLine();
        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 18, 0);
        result.add(separator, constraints);

        JLabel notesTitle = new JLabel("Criterios aplicados");
        notesTitle.setForeground(INK);
        notesTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        constraints.gridy++;
        constraints.insets = new Insets(0, 0, 7, 0);
        result.add(notesTitle, constraints);

        assumptions.setEditable(false);
        assumptions.setLineWrap(true);
        assumptions.setWrapStyleWord(true);
        assumptions.setOpaque(false);
        assumptions.setForeground(MUTED);
        assumptions.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        assumptions.setText("La recomendación y el descuento aparecerán aquí.");
        assumptions.setFocusable(false);
        JScrollPane notes = new JScrollPane(assumptions);
        notes.setBorder(BorderFactory.createEmptyBorder());
        notes.setOpaque(false);
        notes.getViewport().setOpaque(false);
        constraints.gridy++;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, 0, 0);
        result.add(notes, constraints);
        return result;
    }

    private static void addField(JPanel panel, GridBagConstraints constraints, String label, Component field) {
        int row = constraints.gridy + 1;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(MUTED);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        constraints.gridy = row;
        constraints.gridx = 0;
        constraints.weightx = 0.42;
        constraints.insets = new Insets(0, 0, 10, 10);
        panel.add(fieldLabel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.58;
        constraints.insets = new Insets(0, 0, 10, 0);
        panel.add(field, constraints);
    }

    private void calculateFare(ActionEvent event) {
        try {
            double income = Double.parseDouble(incomeField.getText().trim().replace(',', '.'));
            if (!Double.isFinite(income) || income < 0) {
                throw new NumberFormatException("Los ingresos deben ser un importe válido y no negativo.");
            }

            ClientePotencial customer = new ClientePotencial(
                    (Integer) ageField.getValue(),
                    (Integer) flightsField.getValue(),
                    selectedValue(travelerTypeField),
                    selectedValue(flightClassField),
                    selectedValue(destinationField),
                    income,
                    childrenField.isSelected(),
                    parentsField.isSelected()
            );
            ResultadoTarifa result = pricingService.evaluar(customer);
            fareName.setText("<html>" + result.tarifa().getNombre() + "</html>");
            discount.setText(result.tarifa().getDescuentoPorcentaje() + "% de descuento");
            assumptions.setText(result.suposiciones());
            status.setText(" ");
        } catch (NumberFormatException exception) {
            status.setText("Introduce unos ingresos válidos, por ejemplo 25000.");
            incomeField.requestFocusInWindow();
        }
    }

    private static <T> JComboBox<Option<T>> options(T[] values, Function<T, String> label) {
        DefaultComboBoxModel<Option<T>> model = new DefaultComboBoxModel<>();
        for (T value : values) {
            model.addElement(new Option<>(value, label.apply(value)));
        }
        return new JComboBox<>(model);
    }

    private static <T> T selectedValue(JComboBox<Option<T>> combo) {
        return combo.getItemAt(combo.getSelectedIndex()).value();
    }

    private static String travelerLabel(TipoViajero value) {
        return switch (value) {
            case MENOR -> "Menor";
            case ESTUDIANTE_UNIVERSITARIO_DESPLAZADO -> "Estudiante desplazado";
            case TRABAJADOR_JOVEN -> "Trabajador joven";
            case GENERAL -> "General";
        };
    }

    private static String flightClassLabel(ClaseVuelo value) {
        return switch (value) {
            case TURISTA -> "Turista";
            case BUSINESS -> "Business";
        };
    }

    private static String destinationLabel(RegionDestino value) {
        return switch (value) {
            case EUROPA -> "Europa";
            case ASIA -> "Asia";
            case AMERICA -> "América";
            case OTRA -> "Otra";
        };
    }

    private record Option<T>(T value, String label) {
        @Override
        public String toString() {
            return label;
        }
    }

    private static final class JSeparatorLine extends JPanel {
        private JSeparatorLine() {
            setBackground(new Color(199, 216, 205));
            setPreferredSize(new java.awt.Dimension(1, 1));
            setMinimumSize(new java.awt.Dimension(1, 1));
        }
    }
}