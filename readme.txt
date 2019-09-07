���v���O�����̐���
  Ignis ECMAScript Engine �� ECMAScript Language 3rd edition �Ɋ�Â�Java�Ŏ������ꂽ�C���^�[�v���^�ł��B
  Java�ŊJ�����ꂽ�A�v���P�[�V�����̑g�ݍ��ݗp�Ƃ��ĊJ������Ă��܂��B
  ����̃\�t�g�E�F�A�Ƃ��Ă� rhino �� FESI ������܂��B
  -- ECMAScript �Ƃ� --
    ECMAScript �� JavaScript ����u���E�U�ˑ��̋@�\����菜���ɂ��ECMA�ɂ��K�i�����ꂽ�X�N���v�g����ł��B
      (ECMA : European Computer Manufacturer Association - ���[���b�p�d�q�v�Z�@�H�Ɖ�)


���Ƃ肠�������s����
  zip �t�@�C����W�J����� am6.jar ���܂܂�Ă��܂��B
  ���� jar �t�@�C���ɑ΂��Ĉȉ��̃R�}���h�����s���܂��B
  
  >java -jar am6.jar
  
  ���s����ďo�Ă����v�����v�g����͈ȉ��̃R�}���h�����s�ł��܂��B
      �i[]�͔C�ӂ̓��e�Œu��������K�v������܂��j
    @run [�t�@�C����]
      �w�肳�ꂽ�X�N���v�g�t�@�C�������s���܂��B
      ���s���I������܂Ńv�����v�g�͖߂�܂���B
    @start [�t�@�C����]
      �w�肳�ꂽ�X�N���v�g�t�@�C�����V�����X���b�h�ŊJ�n����܂��B
      ���s���I�����Ȃ��Ă��v�����v�g�ɖ߂�܂��B
    @exit
      �v���O�������I�����܂��B
    @mem
      Java�̃������̏�Ԃ�\�����܂��B
    [�v�Z��]
      �v�Z���̕]�����s���܂��B
  
  �����ɃX�N���v�g�t�@�C������n�����ƂŎ��s�Ɠ����ɃX�N���v�g�t�@�C�������s���邱�Ƃ��ł��܂��B
  
  >java -jar am6.jar [�X�N���v�g�t�@�C����]
  
  ��j
  >java -jar am6.jar test/quick_sort.es
  

���r���h���@
  �r���h���s���� am6.jar ���쐬����܂��B�����zip �t�@�C���ɓY�t����� am6.jar �Ɗ�{�I�ɓ������̂ł��B�iJava��ant�̃o�[�W�����ɂ��j
  �r���h�ɂ�J2SDK1.2�ȍ~��jakarta-ant1.5�ȍ~���K�v�ł��B
  JAVA_HOME���ϐ���ANT_HOME���ϐ���ݒ肵������ant�����s���܂��B
  ant_build.bat ��p���Ă��r���h�ł��܂��B
  Eclipse3.0.1�ȍ~��p���Ă��r���h�ł��܂��B
  ���K�\���𗘗p����ɂ� jakarta-ORO �𗘗p���邩�AJ2SDK1.4�ȍ~�𗘗p����K�v������܂��B


���A�v���P�[�V�����ɑg�ݍ���
  ���̃C���^�[�v���^���A�v���P�[�V�����ɑg�ݍ��ނɂ͈ȉ��̎菇���K�v�ł��B
  �@1. ScriptEngine �̃C���X�^���X���쐬���܂��B
    2. �t�@�C���E�X�g���[���̉��ꂩ����X�N���v�g��ǂݍ��� char �̔z��𓾂܂��B
    3. 2�̌��ʂ��� ParsedScript �I�u�W�F�N�g���쐬���܂��B
    3. ParsedScript �I�u�W�F�N�g�����s���܂��B
  �����Ƃ��P���ȗ�͈ȉ��̂悤�ɂȂ�܂��B
    ScriptEngine scriptEngine = new ScriptEngine();
    char[] scriptChars = scriptEngine.loadFromFile(fileName);
    script = scriptEngine.parseScript(scriptChars);
    RuntimeData defaultRuntime = scriptEngine.getExpresser().getDefaultRuntimeData();
    script.execute(defaultRuntime);
  �g�ݍ��ݗp�r�̏ꍇ�A�e�A�v���P�[�V�����𑀍삷�邽�߂̃I�u�W�F�N�g���X�N���v�g���爵����悤�ɂ���K�v������܂����A���̂悤�ɂ��鎖�Ŏ������܂��B
    ScriptEngine scriptEngine = new ScriptEngine();
    char[] scriptChars = scriptEngine.loadFromFile(fileName);
    script = scriptEngine.parseScript(scriptChars);
    RuntimeData defaultRuntime = scriptEngine.getExpresser().getDefaultRuntimeData();
    defaultRuntime.setValue("foo", yourApplicationObject);	//	���̍s�I�I 
    script.execute(defaultRuntime);
    
    ���X�N���v�g����� foo �Ƃ��� yourApplicationObject �ɃA�N�Z�X�ł��܂��B 
      yourApplicationObject �͔C�ӂ̃I�u�W�F�N�g�^���g�p�\�ł��B
  �}���`�X���b�h�A�v���P�[�V�����Ŏg�p����ꍇ�AScriptEngine��AParsedScript �𕡐��쐬����K�v�͂���܂���Bruntime�I�u�W�F�N�g�𕡐����邱�ƂőΉ����܂��B
    //  �S�̂łP��i�ŏ��Ɏ��s����j
    ScriptEngine scriptEngine = new ScriptEngine();
    char[] scriptChars = scriptEngine.loadFromFile(fileName);
    script = scriptEngine.parseScript(scriptChars);
    RuntimeData defaultRuntime = scriptEngine.getExpresser().getDefaultRuntimeData();
    defaultRuntime.setValue("foo", yourApplicationObject);
    
    //  �e�X�̃X���b�h���ňȉ��̏������s���܂��B �iservlet��service���\�b�h�̒��Ƃ��c�j
    RuntimeData defaultRuntime = scriptEngine.getExpresser().getDefaultRuntimeData();
    RuntimeData threadRuntime = new RuntimeData(script.getDefaultRuntime());
    script.execute(defaultRuntime);
  ScriptThread ���g�p���ă}���`�X���b�h���������邱�Ƃ��ł��܂��B
    //  �S�̂łP��i�ŏ��Ɏ��s����j
    ScriptEngine scriptEngine = new ScriptEngine();
    char[] scriptChars = scriptEngine.loadFromFile(fileName);
    script = scriptEngine.parseScript(scriptChars);
    RuntimeData defaultRuntime = scriptEngine.getExpresser().getDefaultRuntimeData();
    defaultRuntime.setValue("foo", yourApplicationObject);
  
    //  �V�����X���b�h���쐬����A���̒��ŃX�N���v�g�����s����܂��B
    script.executeNewThread();  //  �����I�ɐV����RuntimeData���쐬����܂��B
    
�@��������̕��@�ł��}���`�X���b�h�̏ꍇ�A�O���[�o���ϐ������L�����̂Œ��ӂ��K�v�ł��B

  �e�N���X�̊ȒP�Ȑ���
    com.mmatsubara.interpreter.ScriptEngine
      �X�N���v�g�����s���邽�߂̃t�����g�G���h�ƂȂ�܂��B
      �ʏ�̓A�v���P�[�V�����ɂP����Ώ\���ł��B
    com.mmatsubara.interpreter.ParsedScript
      �\�[�X�����͂��ꂽ�X�N���v�g��ێ�����I�u�W�F�N�g�ł��B���Ԃ͍\����͖؂ł��B
      �\�[�X�i�X�N���v�g�j�P�ɑ΂��ĂP�����K�v�ł��B
    com.mmatsubara.expresser.RuntimeData
      �O���[�o���ϐ���W�����o�͂ȂǁA�X���b�h���ɈقȂ����ێ����܂��B
      ���s���s���X���b�h���ɕK�v�ł��B
      �V���O���X���b�h�A�v���P�[�V�����̏ꍇ�́A�����ō쐬����K�v�͂���܂���B


���݊���
  Ignis �͌����_�ł܂��J�����ł��邽�߁AECMAScript Language 3rd edition �ɑ΂��āA�������̔�݊����������Ă��܂��B
  
  �Efor-in�X�e�[�g�����g��a[0]�Ȃǂ̔z��͗񋓂���܂���

  �E�s���̎����Z�~�R�����}�����T�|�[�g����܂���B
      �i�����ɂ͕K���Z�~�R�������K�v�ł��j
      ��Ƃ���ςȂ̂Ŏ�������Ă��Ȃ������ł����A�����炭�T�|�[�g���Ȃ��������}���ꂻ���Ȃ̂œ��ʂ��̂܂܂Ƃ��܂��B

  �EObject �̃R���X�g���N�^�Ɉ�����n���Ă����b�p�[�I�u�W�F�N�g�ƂȂ�܂���B
  
  �Etypeof new Boolean(false) �� "object" ��Ԃ��ׂ��ł����A"boolean"��Ԃ��܂��B
      �i���̃��b�p�[�I�u�W�F�N�g�����l�ł��j

  �E���K�\���ɂ����āAg, i, m �̊e�t���O���T�|�[�g����܂���B

  �E���K�\����ORO�܂���Java1.4�̐��K�\�����C�u�����𗘗p���܂��B���̂���JavaScript�̐��K�\���Ƃ͎኱�݊����ɈႢ������悤�ł��i�������j�B
      ���K�\���ɂ���
        �N���X�p�X����Jakarta-ORO�����݂���΂���𗘗p���܂��B
        ������Ȃ����Java1.4����T�|�[�g���ꂽ���K�\�����C�u���������p����܂��B
        ����������p�ł��Ȃ��ꍇ�͐��K�\���g�p���ɗ�O���������܂��B

  �E���s���G���[�ɂ��Ĉȉ��̃I�u�W�F�N�g�����݂��܂���
      EvalError, RangeError, ReferenceError, SyntaxError, TypeError, URIError

  �EECMAScript �ɂ͂Ȃ� synchronized �X�e�[�g�����g���T�|�[�g����܂��B�\���͈ȉ��̒ʂ�ł��B
      synchronized ( [�I�u�W�F�N�g] ) {
        [�X�e�[�g�����g];
      }

  �E�O���[�o���I�u�W�F�N�g�� writeln, write, print ���\�b�h�ɂĕW���o�͂֏o�͂��邱�Ƃ��ł��܂��B
      writeln(a) �W���o�͂֏o�͌�A���s���s���B
      write(a)   �W���o�͂֏o�͂���B���s�͍s��Ȃ��B
      print(a)   �W���o�͂֏o�͌�A���s���s���B

  �E�O���[�o���I�u�W�F�N�g�� loadClass ���\�b�h�ɂ�Java�̃N���X�����[�h�ł��܂��B�g�����͈ȉ��̒ʂ�ł��B
      JavaDate = loadClass("java.util.Date");
      jdate = new JavaDate();	//	Java�� java.util.Date �N���X�̃C���X�^���X���쐬����
      writeln(jdate);
      jdate.setTime((0).longValue());    // �� ���Q��
      writeln(jdate);
      
      ��Java�N���X�̃��\�b�h���Ăяo���ہA���l�^�������Ƃ��ēn���Ƃ��͐��l�^�ɗp�ӂ����e���\�b�h���g���A�^�ϊ�����K�v������܂��B
        Number.prototype.byteValue()
        Number.prototype.shortValue()
        Number.prototype.intValue()
        Number.prototype.longValue()
        Number.prototype.floatValue()
        Number.prototype.doubleValue()

  �E�萔�ւ̑���͖����ł͂Ȃ���O�ƂȂ�܂��B
  
  �E���̂ق��ɂ������̋C�Â��Ă��Ȃ���݊��������X����Ǝv���܂��B


������̗\��
  �E�e�X�g���ڐ��𑝂₷�B (test/test.es)
�@�E�݊��������コ����B
�@�EJDBC�Ή�����B
  �E�t�@�C�����o�͑Ή�����B
  �EGUI�Ή�(Swing? SWT?)����B
  �E���{�ꃊ�\�[�X�����Ȃ��̂ŉp�ꃊ�\�[�X��p�ӂ���B
  �E�G���[���肪���������Ȃ̂����Ƃ�����B�i�R���p�C���G���[�E���s���G���[�Ƃ��j
  �E�Z�L�����e�B�l����B�i�l���Ă݂�j
  �E�f�o�b�O�@�\�l����B�i���̂����j


���Q�l����
  ECMAScript�d�l�� (�p��EECMAScript�d�l���̎d�l��(pdf)������܂�)
    http://www.ecma-international.org/publications/standards/Ecma-262.htm
  Dynamic Scripting (���{��EJScript�₻�̑��̃X�N���v�g����̃��t�@�����X�Ȃǂ�����܂�)
    http://www.interq.or.jp/student/exeal/dss/
  ECMAScript - on Surface of the Depth - (ECMAScript�̏d���̋������悤�ȃR�[�h����������ڂ��Ă��܂�)
    http://www.kmonos.net/alang/etc/ecmascript.php


�����쌠�\���Ȃ�
  Ignis interpreter
    ECMAScript Language 3rd Edition based
    Copyright (C) 2005 m.matsubara
    HP : http://www.wind.sannet.ne.jp/m_matsu/
    e-mail : m_matsu@wind.sannet.ne.jp
  
  ���̃v���O�����͌���L�p�����ۏ؁A���Q�Ɋւ��ĖƐӂƂ����������Œ񋟂���܂��B
  �����_�ł��̃v���O�����̓A���t�@�ł̂��߁A����d�l���傫���ύX�����\��������܂��B
  
  
