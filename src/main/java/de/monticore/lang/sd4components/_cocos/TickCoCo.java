package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sdbasis._ast.ASTSDBody;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDBodyCoCo;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSequenceDiagramCoCo;
import de.monticore.umlstereotype._ast.ASTStereoValue;
import de.se_rwth.commons.logging.Log;
import de.monticore.lang.sd4components.SD4ComponentsMill;
import java.util.ArrayList;
import java.util.List;

/**
 * When complete, two synchronous messages within the same tick lifetime cannot be sent from the same port.
 */
public class TickCoCo implements SDBasisASTSequenceDiagramCoCo {

  public static final String MESSAGE_ERROR = "0xB500C: "
    + "Two sync ports send within a tick lifetime.";

  @Override
  public void check(ASTSequenceDiagram sd) {
    if(sd.getStereotype().getValuesList().stream().map(ASTStereoValue::getValue).toList().contains("complete")){
      ASTSDBody node = sd.getSDBody();

    for(int i = 0; i < node.getSDElementList().size() - 1; i++) {
      for(int j = i + 1; j < node.getSDElementList().size(); j++) {
        if(SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDTick(node.getSDElement(i)) && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDTick(node.getSDElement(j))) {
          if(SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDTick(node.getSDElement(i + 1))) break;
            List<ASTSDSendMessage> messages = new ArrayList<>();
            int t = i + 1;
            while(t < j) {
            if(SD4ComponentsMill.typeDispatcher().isSDBasisASTSDSendMessage(node.getSDElement(t))) {
              messages.add(SD4ComponentsMill.typeDispatcher().asSDBasisASTSDSendMessage(node.getSDElement(t)));
            }
            t++;
          }
            // handle messages
          List<ASTSDPort> traversedP = new ArrayList<>();
            for (ASTSDSendMessage tr : messages) {
              if(tr.isPresentSDSource() && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(tr.getSDSource())){
                ASTSDPort source = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(tr.getSDSource());
                if(source.getPortSymbol().getTiming().getName().equals("sync")){
                  if(traversedP.contains(source)){
                    Log.error(MESSAGE_ERROR, node.get_SourcePositionStart(), node.get_SourcePositionEnd());
                  }else{
                    traversedP.add(source);
                  }
                }
              }
            }
            break;
          }
        }
      }
    }
  }
}

