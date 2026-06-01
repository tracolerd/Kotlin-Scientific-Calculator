<div align="center">

# 🧮 Kotlin Scientific Calculator

**A feature-rich, dark-themed Scientific Calculator**
built entirely with **Kotlin** and **Java Swing** — zero external dependencies.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.3%2B-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![JVM](https://img.shields.io/badge/JVM-8%2B-ED8B00?logo=openjdk&logoColor=white)](https://adoptium.net)
[![Swing](https://img.shields.io/badge/GUI-Java%20Swing-007396)](#)
[![License](https://img.shields.io/badge/License-MIT-22c55e)](#license)
[![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-64748b)](#)

</div>

---

## ✨ Features

### 🔢 Standard Calculator
| Feature | Description |
|---------|-------------|
| Basic Arithmetic | `+` `−` `×` `÷` with correct operator precedence |
| Sign Toggle (±) | Negate the current number instantly |
| Percentage (%) | Convert to percentage in one tap |
| Parentheses | `(` `)` for grouping complex expressions |
| Chained Operations | `3 + 5 × 2` evaluates to `13`, not `16` |
| Live Expression | Running expression shown above the result |
| Backspace (⌫) | Delete the last entered character |

### 🔬 Scientific Functions
| Button | Operation | Notes |
|--------|-----------|-------|
| `sin` `cos` `tan` | Trigonometry | Respects DEG / RAD mode |
| `asin` `acos` `atan` | Inverse trig | Output in selected angle unit |
| `√` `∛` | Square root & Cube root | Domain-checked |
| `x²` `x³` | Powers of 2 and 3 | |
| `log` `ln` | log₁₀ and natural log | Domain-checked (> 0) |
| `eˣ` `10ˣ` | Exponential functions | |
| `1/x` | Reciprocal | Guards divide-by-zero |
| `\|x\|` | Absolute value | |
| `n!` | Factorial | Integer 0–20 only |
| `^` | Arbitrary power (right-assoc) | e.g. `2 ^ 10 = 1024` |
| `π` `e` | Mathematical constants | Full precision |

### 🎛️ Quality of Life
- **DEG / RAD toggle** — switch angle mode mid-calculation, updates live
- **Calculation History** — last 8 operations shown at the top, newest first
- **Full keyboard support** — use your numpad and keyboard naturally
- **Domain & range errors** — clear messages, no silent `NaN` or crashes
- **Custom recursive-descent parser** — correct precedence without `eval()`
- **Rounded dark UI** — anti-aliased buttons, smooth hover transitions
- **Single JAR output** — share one file, run anywhere Java is installed

---

## 📸 Preview

```
╔════════════════════════════════════════════╗
║  HISTORY                                   ║
║    sqrt(25) = 5                            ║
║    3 ^ 4 = 81                              ║
╠════════════════════════════════════════════╣
║  Angle: DEG                                ║
╠════════════════════════════════════════════╣
║                          3 ^ 4 =           ║
║                               81           ║
╠══════╦══════╦══════╦══════╦═══════════════╣
║ sin  ║ cos  ║ tan  ║ log  ║ ln            ║
║ asin ║ acos ║ atan ║  x²  ║ x³            ║
║  √   ║  ∛   ║ 1/x  ║ |x|  ║ n!            ║
║  eˣ  ║ 10ˣ  ║  π   ║  e   ║  ^            ║
╠══════╬══════╬══════╬══════╬═══════════════╣
║  C   ║  (   ║  )   ║  ⌫   ║  ÷            ║
║  7   ║  8   ║  9   ║  ±   ║  ×            ║
║  4   ║  5   ║  6   ║  %   ║  −            ║
║  1   ║  2   ║  3   ║ D/R  ║  +            ║
╠══════╩══════╬══════╬══════╩═══════════════╣
║      0      ║  .   ║         =            ║
╚═════════════╩══════╩══════════════════════╝
```

---

## 🚀 Getting Started

### Prerequisites

| Tool | Minimum Version | Purpose |
|------|----------------|---------|
| **JDK** | 8+ | Run the JAR |
| **Kotlin compiler** (`kotlinc`) | 1.3+ | Compile from source |

> 💡 If you only want to **run** the pre-built JAR, you only need the JDK.
> You need `kotlinc` only to compile the `.kt` source yourself.

---

### Install the JDK

#### Windows / macOS
Download from [Adoptium (free)](https://adoptium.net) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/).

#### Linux
```bash
# Ubuntu / Debian
sudo apt update && sudo apt install default-jdk

# Fedora / RHEL
sudo dnf install java-21-openjdk-devel

# Arch Linux
sudo pacman -S jdk-openjdk
```

Verify:
```bash
java -version   # should print 1.8+ or 11+
```

---

### Install the Kotlin Compiler

#### All platforms (recommended)
```bash
# Via SDKMAN (easiest, works on Linux/macOS/WSL)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install kotlin
```

#### Linux (apt)
```bash
sudo apt install kotlin
```

#### macOS (Homebrew)
```bash
brew install kotlin
```

#### Windows (Scoop)
```powershell
scoop install kotlin
```

Verify:
```bash
kotlinc -version
```

---

## ▶️ Running the Calculator

### Option A — Compile from source then run

```bash
# 1. Clone the repository
git clone https://github.com/your-username/kotlin-scientific-calculator.git
cd kotlin-scientific-calculator

# 2. Compile  (produces calculator.jar)
kotlinc Calculator.kt -include-runtime -d calculator.jar

# 3. Run
java -jar calculator.jar
```

### Option B — Run a pre-built JAR (if provided in Releases)

```bash
java -jar calculator.jar
```

### One-liner (compile + run)

```bash
kotlinc Calculator.kt -include-runtime -d calculator.jar && java -jar calculator.jar
```

---

## ⌨️ Keyboard Shortcuts

| Key | Action |
|-----|--------|
| `0` – `9` | Input digits |
| `.` | Decimal point |
| `+` | Addition |
| `-` | Subtraction |
| `*` | Multiplication |
| `/` | Division |
| `^` | Power |
| `%` | Percentage |
| `(` `)` | Parentheses |
| `Enter` | Evaluate (`=`) |
| `Backspace` | Delete last character (⌫) |
| `Esc` / `Delete` | Clear all (C) |

---

## 📁 Project Structure

```
kotlin-scientific-calculator/
│
├── Calculator.kt       # Complete source — single self-contained file
├── calculator.jar      # Compiled output (git-ignored)
└── README.md           # You are here
```

> **Tip:** Add `calculator.jar` to your `.gitignore`.

---

## 🛠️ Architecture

```
Calculator.kt
│
├── object Palette                  # All colours in one place
│
├── class ExpressionParser          # Recursive-descent math parser
│   ├── parseAddSub()               #   + and − (lowest precedence)
│   ├── parseMulDiv()               #   × / and % (mid precedence)
│   ├── parsePower()                #   ^ (right-associative)
│   ├── parseUnary()                #   unary minus
│   └── parsePrimary()              #   numbers and ( expr )
│
├── class CalcState                 # Pure logic — no UI dependencies
│   ├── inputDigit / inputOperator  #   input handling
│   ├── evaluate()                  #   full expression evaluation
│   ├── scientific()                #   all scientific functions
│   └── history: MutableList        #   last 20 results
│
├── class CalcButton                # Custom rounded Swing button
│   └── paintComponent()            #   anti-aliased RoundRect
│
├── class HistoryPanel              # Scrollable history list
│
└── class CalculatorWindow          # Main JFrame
    ├── buildTop()                  #   history + mode bar + display
    ├── buildButtons()              #   9-row GridBagLayout grid
    ├── btn()                       #   button factory (id → CalcButton)
    ├── dispatch()                  #   routes all input to CalcState
    └── setupKeys()                 #   keyboard → dispatch()
```

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 1.3+ (JVM target) |
| GUI Framework | Java Swing (`javax.swing`) |
| Layout | `GridBagLayout` (button grid) + `BoxLayout` (display) |
| Expression Parsing | Hand-written recursive-descent — no `eval()` |
| Rendering | `Graphics2D` with `VALUE_ANTIALIAS_ON` for round buttons |
| Distribution | Single fat JAR via `-include-runtime` |

---

## 🤝 Contributing

All contributions are welcome — bug fixes, new features, or UI improvements!

```bash
# 1. Fork and clone
git clone https://github.com/your-username/kotlin-scientific-calculator.git
cd kotlin-scientific-calculator

# 2. Create a branch
git checkout -b feature/my-feature

# 3. Make changes, then compile & test
kotlinc Calculator.kt -include-runtime -d calculator.jar
java -jar calculator.jar

# 4. Commit and push
git add Calculator.kt
git commit -m "feat: add my feature"
git push origin feature/my-feature

# 5. Open a Pull Request on GitHub
```

---

## 📝 .gitignore

```gitignore
# Compiled output
*.jar
*.class

# IDE files
.idea/
*.iml
.vscode/
build/
out/
```

---

## 📜 License

This project is released under the **MIT License** — free to use, modify, and distribute.

---

<div align="center">

Made by Faiyaz using Kotlin

*"Kotlin — a language that makes Java developers fall in love again."*

</div>
