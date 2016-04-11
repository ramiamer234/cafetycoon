package org.vaadin.samples.cafetycoon.ui.dashboard;

import com.vaadin.addon.charts.Chart;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;
import org.vaadin.samples.cafetycoon.ui.dashboard.components.MessageBoard;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class CafeOverviewDesign extends VerticalLayout {
	protected Label cafeName;
	protected Label cafeAddress;
	protected Label outOfStock;
	protected MessageBoard personnel;
	protected Grid salesData;
	protected Chart beanStock;
	protected Button restock50;
	protected Button restock100;
	protected Button restock200;

	public CafeOverviewDesign() {
		Design.read(this);
	}
}