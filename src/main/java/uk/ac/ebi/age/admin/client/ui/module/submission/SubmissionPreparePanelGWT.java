package uk.ac.ebi.age.admin.client.ui.module.submission;

import uk.ac.ebi.age.admin.client.log.ROJSLogNode;
import uk.ac.ebi.age.admin.client.ui.module.log.LogWindow3;
import uk.ac.ebi.age.admin.client.ui.module.submission.NewDMPanel.RemoveListener;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.SubmissionConstants;
import uk.ac.ebi.age.ext.log.LogNode;
import uk.ac.ebi.age.ext.submission.Status;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SubmissionPreparePanelGWT extends VLayout
{
 private int           n     = 1;
 private long          key   = System.currentTimeMillis();
 private int           nMods = 1;
 private VerticalPanel panel;

 public SubmissionPreparePanelGWT()
 {
  setAutoWidth();
  setLayoutLeftMargin(15);
  setLayoutTopMargin(15);
  setMembersMargin(15);
  setShowCustomScrollbars(false);
  setOverflow(Overflow.AUTO);

  setHeight100();
  setWidth(700);

  setBorder("1px solid #6a5a6a");

  final DecoratorPanel decp = new DecoratorPanel();
  // decp.setHeight("100%");

  decp.setWidth("650px");

  final WidgetCanvas wc = new WidgetCanvas(decp);
  wc.setOverflow(Overflow.VISIBLE);
  // final Layout wc = new Layout();
  // wc.setHeight100();
//  wc.setBorder("1px dotted red");
//  wc.setShowCustomScrollbars(false);
  // wc.setWidth(800);
  // wc.addMember(decp);
  
  final Layout l = new Layout();

  l.setHeight100();
  l.setWidth(700);
  l.setShowCustomScrollbars(false);
  l.setOverflow(Overflow.AUTO);
  l.addMember(wc);
//  l.setBorder("1px dotted navy");

  final FormPanel form = new FormPanel();
  form.setAction("upload");
  form.setEncoding(FormPanel.ENCODING_MULTIPART);
  form.setMethod(FormPanel.METHOD_POST);

  // form.setWidth("501px");

  panel = new VerticalPanel();
  panel.setSpacing(10);
  // panel.setWidth("502px");
  form.setWidget(panel);

  panel.add(new Hidden(Constants.serviceHandlerParameter, Constants.SUBMISSON_COMMAND));
  panel.add(new Hidden(SubmissionConstants.SUBMISSON_KEY, String.valueOf(key)));
  panel.add(new Hidden(SubmissionConstants.SUBMISSON_STATUS, Status.NEW.name()));

  FlexTable btPan = new FlexTable();
  btPan.setCellSpacing(6);
  btPan.setWidth("100%");
  FlexCellFormatter cellFormatter = btPan.getFlexCellFormatter();

  panel.add(btPan);

  
  
  final NewDMPanel.RemoveListener rmListener = new RemoveListener()
  {
   @Override
   public void removed(Widget w)
   {
    renumberPanels();
   }
  };

  int col=0;
  
  Label voLbl = new Label("Verify only");
  cellFormatter.setHorizontalAlignment(0, col, HasHorizontalAlignment.ALIGN_RIGHT);
  btPan.setWidget(0, col, voLbl);

  CheckBox verifOnly = new CheckBox();
  verifOnly.setName(SubmissionConstants.VERIFY_ONLY);
  verifOnly.setTitle("Verify only");
  cellFormatter.setHorizontalAlignment(0, ++col, HasHorizontalAlignment.ALIGN_LEFT);
  btPan.setWidget(0, col, verifOnly);
  
  cellFormatter.setHorizontalAlignment(0, ++col, HasHorizontalAlignment.ALIGN_CENTER);
  Button addBt = new Button("Add File", new com.google.gwt.event.dom.client.ClickHandler()
  {
   public void onClick(com.google.gwt.event.dom.client.ClickEvent event)
   {
    panel.insert(new NewFilePanel(n++, rmListener), panel.getWidgetCount() - 1);
    wc.adjustForContent(true);

    renumberPanels();
   }
  });
  btPan.setWidget(0, col, addBt);

  cellFormatter.setHorizontalAlignment(0, ++col, HasHorizontalAlignment.ALIGN_CENTER);
  addBt = new Button("Add Data Module", new com.google.gwt.event.dom.client.ClickHandler()
  {
   public void onClick(com.google.gwt.event.dom.client.ClickEvent event)
   {
    panel.insert(new NewDMPanel(n++, rmListener), nMods + 6);
    nMods++;
    wc.adjustForContent(true);
    // decp.adjustForContent(true);

    renumberPanels();
   }
  });
  btPan.setWidget(0, col, addBt);

  panel.add(new Label("Submission description:"));

  final TextArea tb = new TextArea();
  tb.setName(SubmissionConstants.SUBMISSON_DESCR);
  tb.setWidth("97%");
  panel.add(tb);

  panel.add(new NewDMPanel(n++, rmListener));

  Button sbmBt = new Button("Submit", new com.google.gwt.event.dom.client.ClickHandler()
  {
   public void onClick(com.google.gwt.event.dom.client.ClickEvent event)
   {
    form.submit();
   }
  });

  panel.add(sbmBt);
  panel.setCellHorizontalAlignment(sbmBt, HasHorizontalAlignment.ALIGN_RIGHT);

  form.addSubmitHandler(new FormPanel.SubmitHandler()
  {
   public void onSubmit(SubmitEvent event)
   {
    String err = "";

    int ndm = 0;
    for(Widget w : panel)
    {
     if(w instanceof NewDMPanel)
     {
      ndm++;

      NewDMPanel dmp = (NewDMPanel) w;

      if(dmp.getDescription().trim().length() == 0)
       err += "Description of data module " + ndm + " is empty\n";

      if(dmp.getFile().trim().length() == 0)
       err += "File for data module " + ndm + " is not selected\n";
     }
     else if(w instanceof NewFilePanel)
     {
      ndm++;

      NewFilePanel dmp = (NewFilePanel) w;

      if(dmp.getID().trim().length() == 0)
       err += "ID for file " + ndm + " is not specified\n";

      if(dmp.getFile().trim().length() == 0)
       err += "File for data module " + ndm + " is not selected\n";
     }
     else if(w instanceof TextArea)
     {
      if(((TextArea) w).getText().trim().length() == 0)
       err += "Submission description is empty\n";
     }

    }

    if(err.length() > 0)
    {
     Window.alert("ERROR:\n" + err);
     event.cancel();
    }

   }
  });

  form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler()
  {
   public void onSubmitComplete(SubmitCompleteEvent event)
   {
    String txt = event.getResults();

    if(txt.indexOf("OK-" + key) == -1)
    {
     SC.warn("Error occured. Possibly you are not logged on or your session is expired");
     return;
    }
    
    int posB = txt.indexOf(Constants.beginJSONSign);
    int posE = txt.lastIndexOf(Constants.endJSONSign);

    txt = txt.substring(posB + Constants.beginJSONSign.length(), posE);

    LogNode rLn = ROJSLogNode.convert(txt);

    new LogWindow3("Submission creation log", rLn).show();

   }
  });

  decp.setWidget(form);

  addMember(wc);
  // addMember(form);

 }

 private void renumberPanels()
 {
  n = 0;
  nMods = 0;

  for(Widget w : panel)
  {
   if(w instanceof NewDMPanel)
   {
    n++;
    nMods++;

    NewDMPanel dmp = (NewDMPanel) w;

    dmp.setOrder(n);

   }
   else if(w instanceof NewFilePanel)
   {
    n++;

    NewFilePanel fp = (NewFilePanel) w;

    fp.setOrder(n);
   }

  }

 }

}
