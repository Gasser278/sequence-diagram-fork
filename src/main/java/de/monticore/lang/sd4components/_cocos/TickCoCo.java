package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDTick;
import de.monticore.lang.sd4components._ast.ASTSDSequenceDiagram;
import de.monticore.lang.sd4components._cocos.SD4ComponentsASTSequenceDiagramCoCo;
import de.se_rwth.commons.logging.Log;

/**
 * Ensures that ticks are correctly modeled in a sequence diagram.
 */
public class TickCoCo implements SD4ComponentsASTSequenceDiagramCoCo {

  public static final String MESSAGE_ERROR = "0xB5009: "
    + "Tick is not properly modeled in the sequence diagram.";

  @Override
  public void check(ASTSDSequenceDiagram node) {
    node.getSDBody().streamSDElements()
      .filter(e -> e instanceof ASTSDTick)
      .forEach(tick -> {
        if (!isValidTick((ASTSDTick) tick)) {
          Log.error(MESSAGE_ERROR, tick.get_SourcePositionStart(), tick.get_SourcePositionEnd());
        }
      });
  }

  private boolean isValidTick(ASTSDTick tick) {
    //TASK: Implement tick validation method
    return true;
  }
}

