import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.border.MatteBorder
import java.awt.*
import java.awt.event.*
import kotlin.math.*

// ══════════════════════════════════════════════════════════════════════════════
//  Kotlin Scientific Calculator  — Swing GUI
//  Compatible: Kotlin 1.3+  |  JDK 8+
//
//  Compile :  kotlinc Calculator.kt -include-runtime -d calculator.jar
//  Run     :  java -jar calculator.jar
// ══════════════════════════════════════════════════════════════════════════════

// ── Colour palette ────────────────────────────────────────────────────────────
object Palette {
    val BG_WINDOW   = Color(0x0F, 0x0E, 0x17)
    val BG_DISPLAY  = Color(0x16, 0x21, 0x3E)
    val BG_HISTORY  = Color(0x12, 0x12, 0x2A)
    val BG_MODEBAR  = Color(0x12, 0x12, 0x2A)
    val BTN_NUM     = Color(0x2D, 0x35, 0x61)
    val BTN_OP      = Color(0xE9, 0x45, 0x60)
    val BTN_FUNC    = Color(0x25, 0x35, 0x55)
    val BTN_SCI     = Color(0x16, 0x21, 0x3E)
    val BTN_EQUAL   = Color(0x0F, 0x34, 0x60)
    val BTN_CLEAR   = Color(0xC7, 0x36, 0x52)
    val BTN_MODE    = Color(0x1A, 0x2A, 0x3A)
    val FG_MAIN     = Color(0xE2, 0xE8, 0xF0)
    val FG_SUB      = Color(0x6C, 0x7A, 0x9C)
    val FG_SCI      = Color(0x7A, 0xB4, 0xD0)
    val FG_FUNC     = Color(0xA0, 0xB4, 0xD0)
    val FG_ACCENT   = Color(0xE9, 0x45, 0x60)
    val FG_WHITE    = Color.WHITE
    val FG_HIST     = Color(0x5A, 0x6A, 0x8A)
    val BORDER_SEP  = Color(0x2A, 0x2A, 0x4A)
}

// ── Expression parser (recursive-descent, Kotlin 1.3 compatible) ─────────────
// Grammar (precedence low -> high):
//   expr    -> addSub
//   addSub  -> mulDiv { ('+'|'-') mulDiv }
//   mulDiv  -> power  { ('*'|'/'|'%') power }
//   power   -> unary  ('^' unary)?       right-associative
//   unary   -> '-' unary | primary
//   primary -> number | '(' expr ')'
class ExpressionParser(src: String) {
    private var pos = 0
    private val expr: String = src
        .replace("\u00d7", "*")
        .replace("\u00f7", "/")
        .replace("\u2212", "-")
        .replace("\\s".toRegex(), "")

    fun evaluate(): Double {
        val v = parseAddSub()
        if (pos != expr.length) throw IllegalArgumentException("Unexpected char at $pos")
        return v
    }

    private fun peek(): Char? = if (pos < expr.length) expr[pos] else null
    private fun consume(): Char = expr[pos++]

    private fun parseAddSub(): Double {
        var v = parseMulDiv()
        while (true) {
            v = when (peek()) {
                '+' -> { consume(); v + parseMulDiv() }
                '-' -> { consume(); v - parseMulDiv() }
                else -> return v
            }
        }
    }

    private fun parseMulDiv(): Double {
        var v = parsePower()
        while (true) {
            v = when (peek()) {
                '*' -> { consume(); v * parsePower() }
                '/' -> { consume()
                         val d = parsePower()
                         if (d == 0.0) throw ArithmeticException("Division by zero")
                         v / d }
                '%' -> { consume()
                         val d = parsePower()
                         if (d == 0.0) throw ArithmeticException("Modulo by zero")
                         v % d }
                else -> return v
            }
        }
    }

    private fun parsePower(): Double {
        val base = parseUnary()
        if (peek() == '^') { consume(); return base.pow(parsePower()) }
        return base
    }

    private fun parseUnary(): Double {
        if (peek() == '-') { consume(); return -parseUnary() }
        if (peek() == '+') { consume(); return +parseUnary() }
        return parsePrimary()
    }

    private fun parsePrimary(): Double {
        if (peek() == '(') {
            consume()
            val v = parseAddSub()
            if (peek() == ')') consume()
            else throw IllegalArgumentException("Missing closing )")
            return v
        }
        return parseNumber()
    }

    private fun parseNumber(): Double {
        val start = pos
        if (peek() == '-' || peek() == '+') consume()
        while (peek()?.isDigit() == true || peek() == '.') consume()
        if ((peek() == 'e' || peek() == 'E') && pos > start) {
            consume()
            if (peek() == '+' || peek() == '-') consume()
            while (peek()?.isDigit() == true) consume()
        }
        if (start == pos) throw IllegalArgumentException("Expected number at pos $pos")
        return expr.substring(start, pos).toDouble()
    }
}

// ── Calculator state & logic ──────────────────────────────────────────────────
class CalcState {
    var expression   = ""
    var currentInput = "0"
    var newNumber    = true
    var useDegrees   = true
    var errorState   = false
    val history      = mutableListOf<String>()

    private fun toRad(deg: Double) = deg * PI / 180.0
    private fun toDeg(rad: Double) = rad * 180.0 / PI

    fun fmt(v: Double): String {
        if (v.isNaN())      return "Not a Number"
        if (v.isInfinite()) return if (v > 0) "+Infinity" else "-Infinity"
        if (v == floor(v) && abs(v) < 1e15) return v.toLong().toString()
        val s = "%.10g".format(v)
        return s.trimEnd('0').trimEnd('.')
    }

    fun inputDigit(d: String) {
        if (errorState) clear()
        if (d == "." && !newNumber && currentInput.contains('.')) return
        currentInput = if (newNumber) {
            newNumber = false
            if (d == ".") "0." else d
        } else {
            if (currentInput == "0" && d != ".") d else currentInput + d
        }
    }

    fun inputOperator(op: String) {
        if (errorState) return
        if (expression.isNotEmpty() && !newNumber) {
            runCatching {
                val r = ExpressionParser(expression + currentInput).evaluate()
                currentInput = fmt(r)
            }
        }
        expression = "$currentInput $op "
        newNumber = true
    }

    data class EvalResult(val display: String, val sub: String)

    fun evaluate(): EvalResult {
        val full = expression + currentInput
        val sub  = "$full ="
        return try {
            val r   = ExpressionParser(full).evaluate()
            val res = fmt(r)
            addHistory("$full = $res")
            expression = ""; newNumber = true
            EvalResult(res, sub).also { currentInput = res }
        } catch (e: ArithmeticException) {
            errorState = true
            EvalResult(e.message ?: "Divide Error", sub)
        } catch (e: Exception) {
            errorState = true
            EvalResult("Error", sub)
        }
    }

    fun clear()     { expression = ""; currentInput = "0"; newNumber = true; errorState = false }
    fun backspace() { if (newNumber || errorState) { clear(); return }
                      currentInput = if (currentInput.length > 1) currentInput.dropLast(1) else "0" }
    fun negate()    { if (errorState) return; currentInput.toDoubleOrNull()?.let { currentInput = fmt(-it) } }
    fun percent()   { if (errorState) return; currentInput.toDoubleOrNull()?.let { currentInput = fmt(it / 100.0) } }
    fun constant(c: String) { currentInput = if (c == "PI") fmt(PI) else fmt(E); newNumber = false }

    data class SciResult(val display: String, val histEntry: String, val ok: Boolean, val isError: Boolean = false)

    fun scientific(func: String): SciResult {
        if (errorState) return SciResult("", "", false)
        val v = currentInput.toDoubleOrNull() ?: return SciResult("", "", false)
        val angle = if (useDegrees) toRad(v) else v

        fun ok(r: Double, lbl: String = "$func($currentInput)"): SciResult {
            val res = fmt(r); return SciResult(res, "$lbl = $res", true)
        }
        fun domErr() = SciResult("Domain Error", "", true, true)
        fun undef()  = SciResult("Undefined",    "", true, true)

        return when (func) {
            "sin"    -> ok(sin(angle))
            "cos"    -> ok(cos(angle))
            "tan"    -> if (abs(cos(angle)) < 1e-12) undef() else ok(tan(angle))
            "asin"   -> if (v !in -1.0..1.0) domErr() else ok(asin(v).let { if (useDegrees) toDeg(it) else it })
            "acos"   -> if (v !in -1.0..1.0) domErr() else ok(acos(v).let { if (useDegrees) toDeg(it) else it })
            "atan"   -> ok(atan(v).let { if (useDegrees) toDeg(it) else it })
            "sqrt"   -> if (v < 0) domErr() else ok(sqrt(v), "sqrt($currentInput)")
            "cbrt"   -> ok(sign(v) * abs(v).pow(1.0/3.0), "cbrt($currentInput)")
            "x^2"    -> ok(v * v,       "$currentInput^2")
            "x^3"    -> ok(v * v * v,   "$currentInput^3")
            "log"    -> if (v <= 0) domErr() else ok(log10(v))
            "ln"     -> if (v <= 0) domErr() else ok(ln(v))
            "e^x"    -> ok(exp(v))
            "10^x"   -> ok(10.0.pow(v))
            "1/x"    -> if (v == 0.0) undef() else ok(1.0 / v)
            "abs"    -> ok(abs(v), "|$currentInput|")
            "fact"   -> {
                if (v < 0 || v != floor(v) || v > 20) return domErr()
                var f = 1L; for (i in 2..v.toLong()) f *= i
                ok(f.toDouble(), "$currentInput!")
            }
            else -> SciResult("", "", false)
        }
    }

    private fun addHistory(entry: String) {
        if (history.size >= 20) history.removeAt(0)
        history.add(entry)
    }
}

// ── Rounded button ────────────────────────────────────────────────────────────
class CalcButton(text: String, private val bg: Color, fg: Color, sz: Int) : JButton(text) {
    private val hover = bg.darker()
    init {
        font                = Font("Courier New", Font.BOLD, sz)
        foreground          = fg
        isFocusPainted      = false
        isBorderPainted     = false
        isContentAreaFilled = false
        isOpaque            = false
        background          = bg
        cursor              = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent) { background = hover; repaint() }
            override fun mouseExited (e: MouseEvent) { background = bg;    repaint() }
        })
    }
    override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = background
        g2.fillRoundRect(0, 0, width, height, 10, 10)
        super.paintComponent(g)
    }
}

// ── History panel ─────────────────────────────────────────────────────────────
class HistoryPanel : JPanel(BorderLayout()) {
    private val model = DefaultListModel<String>()
    private val list  = JList(model)
    init {
        background   = Palette.BG_HISTORY
        border       = MatteBorder(0, 0, 1, 0, Palette.BORDER_SEP)
        preferredSize = Dimension(0, 95)

        val title = JLabel("  HISTORY").apply {
            font       = Font("Courier New", Font.BOLD, 11)
            foreground = Color(0x4A, 0x5A, 0x7A)
            border     = EmptyBorder(4, 0, 2, 0)
        }
        list.apply {
            background          = Palette.BG_HISTORY
            foreground          = Palette.FG_HIST
            font                = Font("Courier New", Font.PLAIN, 11)
            visibleRowCount     = 4
        }
        val scroll = JScrollPane(list).apply {
            border = null
            viewport.background = Palette.BG_HISTORY
        }
        add(title,  BorderLayout.NORTH)
        add(scroll, BorderLayout.CENTER)
    }
    fun refresh(items: List<String>) {
        model.clear()
        items.asReversed().take(8).forEach { model.addElement("  $it") }
    }
}

// ── Main window ───────────────────────────────────────────────────────────────
class CalculatorWindow : JFrame("Kotlin Scientific Calculator") {

    private val state   = CalcState()
    private val histPnl = HistoryPanel()

    private val subLbl = JLabel(" ").apply {
        font                = Font("Courier New", Font.PLAIN, 13)
        foreground          = Palette.FG_SUB
        horizontalAlignment = SwingConstants.RIGHT
        border              = EmptyBorder(0, 0, 0, 4)
    }
    private val mainLbl = JLabel("0").apply {
        font                = Font("Courier New", Font.BOLD, 40)
        foreground          = Palette.FG_MAIN
        horizontalAlignment = SwingConstants.RIGHT
        border              = EmptyBorder(4, 0, 8, 4)
    }
    private val angleBtn = JLabel("DEG").apply {
        font       = Font("Courier New", Font.BOLD, 12)
        foreground = Palette.FG_ACCENT
        cursor     = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        border     = EmptyBorder(4, 10, 4, 10)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) { toggleAngle() }
        })
    }

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable           = false
        preferredSize         = Dimension(460, 760)
        contentPane.background = Palette.BG_WINDOW
        contentPane.layout    = BorderLayout()
        contentPane.add(buildTop(),    BorderLayout.NORTH)
        contentPane.add(buildButtons(), BorderLayout.CENTER)
        setupKeys()
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }

    // ── Top (history + mode bar + display) ────────────────────────────
    private fun buildTop(): JPanel {
        // Mode bar
        val modebar = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0)).apply {
            background = Palette.BG_MODEBAR
            border     = MatteBorder(0, 0, 1, 0, Palette.BORDER_SEP)
            add(JLabel("  Angle: ").apply {
                font       = Font("Courier New", Font.PLAIN, 11)
                foreground = Color(0x4A, 0x5A, 0x7A)
            })
            add(angleBtn)
        }
        // Display
        val display = JPanel().apply {
            layout     = BoxLayout(this, BoxLayout.Y_AXIS)
            background = Palette.BG_DISPLAY
            border     = EmptyBorder(10, 16, 12, 16)
            add(subLbl.also  { it.maximumSize = Dimension(Int.MAX_VALUE, 20) })
            add(mainLbl.also { it.maximumSize = Dimension(Int.MAX_VALUE, 58) })
        }
        return JPanel(BorderLayout()).apply {
            isOpaque = false
            add(histPnl, BorderLayout.NORTH)
            add(modebar, BorderLayout.CENTER)
            add(display, BorderLayout.SOUTH)
        }
    }

    // ── Button grid ───────────────────────────────────────────────────
    private fun buildButtons(): JPanel {
        val grid = JPanel(GridBagLayout()).apply { isOpaque = false }
        val gc   = GridBagConstraints().apply {
            fill    = GridBagConstraints.BOTH
            weightx = 1.0; weighty = 1.0
            insets  = Insets(3, 3, 3, 3)
        }

        fun add(btn: JButton, col: Int, row: Int, span: Int = 1) {
            gc.gridx = col; gc.gridy = row; gc.gridwidth = span
            grid.add(btn, gc)
        }

        // ── Row 0-3: Scientific ──────────────────────────────────────
        // Row 0
        add(btn("sin",  "sci"), 0, 0); add(btn("cos",  "sci"), 1, 0)
        add(btn("tan",  "sci"), 2, 0); add(btn("log",  "sci"), 3, 0)
        add(btn("ln",   "sci"), 4, 0)
        // Row 1
        add(btn("asin", "sci"), 0, 1); add(btn("acos", "sci"), 1, 1)
        add(btn("atan", "sci"), 2, 1); add(btn("x^2",  "sci"), 3, 1)
        add(btn("x^3",  "sci"), 4, 1)
        // Row 2
        add(btn("sqrt", "sci"), 0, 2); add(btn("cbrt", "sci"), 1, 2)
        add(btn("1/x",  "sci"), 2, 2); add(btn("abs",  "sci"), 3, 2)
        add(btn("fact", "sci"), 4, 2)
        // Row 3
        add(btn("e^x",  "sci"), 0, 3); add(btn("10^x", "sci"), 1, 3)
        add(btn("PI",   "sci"), 2, 3); add(btn("E",    "sci"), 3, 3)
        add(btn("^",    "sci"), 4, 3)

        // ── Row 4: C  (  )  DEL  div ────────────────────────────────
        add(btn("C",   "clear"), 0, 4); add(btn("(",  "func"),  1, 4)
        add(btn(")",   "func"),  2, 4); add(btn("DEL","func"),  3, 4)
        add(btn("div", "op"),    4, 4)

        // ── Row 5: 7 8 9 +/- mul ────────────────────────────────────
        add(btn("7", "num"), 0, 5); add(btn("8", "num"), 1, 5)
        add(btn("9", "num"), 2, 5); add(btn("+/-","func"), 3, 5)
        add(btn("mul","op"), 4, 5)

        // ── Row 6: 4 5 6 % sub ──────────────────────────────────────
        add(btn("4", "num"), 0, 6); add(btn("5", "num"), 1, 6)
        add(btn("6", "num"), 2, 6); add(btn("%",  "func"), 3, 6)
        add(btn("sub","op"), 4, 6)

        // ── Row 7: 1 2 3 DEG/RAD add ────────────────────────────────
        add(btn("1", "num"), 0, 7); add(btn("2", "num"), 1, 7)
        add(btn("3", "num"), 2, 7); add(btn("MODE","mode"), 3, 7)
        add(btn("add","op"), 4, 7)

        // ── Row 8: 0 (wide)  .  = (wide) ────────────────────────────
        add(btn("0", "num"),    0, 8, 2)
        gc.gridwidth = 1
        add(btn(".", "num"),    2, 8)
        add(btn("=", "equal"),  3, 8, 2)

        return JPanel(BorderLayout()).apply {
            isOpaque = false
            border   = EmptyBorder(8, 10, 12, 10)
            add(grid)
        }
    }

    // ── Button factory ────────────────────────────────────────────────
    private fun btn(id: String, type: String): JButton {
        val label = when (id) {
            "div"  -> "\u00f7"          // ÷
            "mul"  -> "\u00d7"          // ×
            "sub"  -> "\u2212"          // −
            "add"  -> "+"
            "DEL"  -> "\u232b"          // ⌫
            "+/-"  -> "\u00b1"          // ±
            "sqrt" -> "\u221a"          // √
            "cbrt" -> "\u221b"          // ∛
            "x^2"  -> "x\u00b2"        // x²
            "x^3"  -> "x\u00b3"        // x³
            "abs"  -> "|x|"
            "fact" -> "n!"
            "e^x"  -> "e\u02e3"        // eˣ
            "10^x" -> "10\u02e3"       // 10ˣ
            "PI"   -> "\u03c0"         // π
            "E"    -> "e"
            "MODE" -> "DEG/RAD"
            else   -> id
        }
        val (bg, fg, sz) = when (type) {
            "num"   -> Triple(Palette.BTN_NUM,   Palette.FG_WHITE, 16)
            "op"    -> Triple(Palette.BTN_OP,    Palette.FG_WHITE, 18)
            "func"  -> Triple(Palette.BTN_FUNC,  Palette.FG_FUNC,  15)
            "sci"   -> Triple(Palette.BTN_SCI,   Palette.FG_SCI,   12)
            "equal" -> Triple(Palette.BTN_EQUAL, Palette.FG_WHITE, 18)
            "clear" -> Triple(Palette.BTN_CLEAR, Palette.FG_WHITE, 16)
            "mode"  -> Triple(Palette.BTN_MODE,  Color(0x70, 0x90, 0xB0), 11)
            else    -> Triple(Palette.BTN_NUM,   Palette.FG_WHITE, 15)
        }
        return CalcButton(label, bg, fg, sz).also { b ->
            b.addActionListener { dispatch(id) }
        }
    }

    // ── Dispatch ──────────────────────────────────────────────────────
    private fun dispatch(id: String) {
        when (id) {
            "C"    -> { state.clear();     sync() }
            "DEL"  -> { state.backspace(); sync() }
            "+/-"  -> { state.negate();    sync() }
            "%"    -> { state.percent();   sync() }
            "="    -> { val r = state.evaluate()
                         mainLbl.text = r.display
                         subLbl.text  = r.sub
                         histPnl.refresh(state.history); return }
            "add"  -> { state.inputOperator("+");                 sync() }
            "sub"  -> { state.inputOperator("\u2212");            sync() }
            "mul"  -> { state.inputOperator("\u00d7");            sync() }
            "div"  -> { state.inputOperator("\u00f7");            sync() }
            "^"    -> { state.inputOperator("^");                 sync() }
            "PI","E" -> { state.constant(id);                     sync() }
            "MODE" -> { toggleAngle(); return }
            "(",")" -> {
                if (state.newNumber) { state.currentInput = id; state.newNumber = false }
                else state.currentInput += id
                sync()
            }
            "sin","cos","tan","asin","acos","atan",
            "sqrt","cbrt","x^2","x^3","log","ln",
            "e^x","10^x","1/x","abs","fact" -> {
                val r = state.scientific(id)
                if (r.ok) {
                    state.currentInput = r.display
                    state.newNumber    = true
                    state.errorState   = r.isError
                    if (r.histEntry.isNotEmpty()) {
                        state.addHistoryPublic(r.histEntry)
                        histPnl.refresh(state.history)
                    }
                    mainLbl.text = r.display
                    subLbl.text  = if (r.isError) r.display else r.histEntry
                }
            }
            else -> { state.inputDigit(id); sync() }
        }
    }

    private fun sync() {
        mainLbl.text = state.currentInput.ifEmpty { "0" }
        subLbl.text  = state.expression.ifEmpty { " " }
    }

    private fun toggleAngle() {
        state.useDegrees = !state.useDegrees
        angleBtn.text       = if (state.useDegrees) "DEG" else "RAD"
        angleBtn.foreground = if (state.useDegrees) Palette.FG_ACCENT else Color(0x60, 0xC0, 0x80)
    }

    // ── Keyboard ──────────────────────────────────────────────────────
    private fun setupKeys() {
        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                when {
                    e.keyCode in KeyEvent.VK_0..KeyEvent.VK_9          -> dispatch((e.keyCode - KeyEvent.VK_0).toString())
                    e.keyCode in KeyEvent.VK_NUMPAD0..KeyEvent.VK_NUMPAD9 -> dispatch((e.keyCode - KeyEvent.VK_NUMPAD0).toString())
                    e.keyChar == '.'                                    -> dispatch(".")
                    e.keyChar == '+'                                    -> dispatch("add")
                    e.keyChar == '-'                                    -> dispatch("sub")
                    e.keyChar == '*'                                    -> dispatch("mul")
                    e.keyChar == '/'                                    -> dispatch("div")
                    e.keyChar == '^'                                    -> dispatch("^")
                    e.keyChar == '%'                                    -> dispatch("%")
                    e.keyChar == '('                                    -> dispatch("(")
                    e.keyChar == ')'                                    -> dispatch(")")
                    e.keyCode == KeyEvent.VK_ENTER                     -> dispatch("=")
                    e.keyCode == KeyEvent.VK_BACK_SPACE                -> dispatch("DEL")
                    e.keyCode == KeyEvent.VK_ESCAPE || e.keyCode == KeyEvent.VK_DELETE -> dispatch("C")
                }
            }
        })
        isFocusable = true
        requestFocusInWindow()
    }
}

// Extension to expose addHistory from outside (needed for sci results)
fun CalcState.addHistoryPublic(entry: String) {
    if (history.size >= 20) history.removeAt(0)
    history.add(entry)
}

// ── Entry point ───────────────────────────────────────────────────────────────
fun main() {
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())
    SwingUtilities.invokeLater { CalculatorWindow() }
}
