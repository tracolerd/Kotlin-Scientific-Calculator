<p align="center">
  <img src="assets/readme-hero.svg" alt="Kotlin Scientific Calculator — custom animated project banner" width="100%" />
</p>

<h1 align="center">Kotlin Scientific Calculator</h1>

<p align="center">
  <strong>Scientific functions in a focused desktop interface.</strong><br/>
  A Kotlin and Swing calculator with grouped expressions, trigonometry, degree/radian mode and a practical standard-calculator workflow.
</p>

<p align="center">
  <a href="https://kotlinlang.org/"><img src="https://img.shields.io/badge/Kotlin-Language-7F52FF?style=for-the-badge&amp;logo=kotlin&amp;logoColor=white" alt="Kotlin — Language" /></a>
  <a href="https://adoptium.net/"><img src="https://img.shields.io/badge/JVM-Runtime-ED8B00?style=for-the-badge&amp;logo=openjdk&amp;logoColor=white" alt="JVM — Runtime" /></a>
  <a href="https://docs.oracle.com/javase/tutorial/uiswing/"><img src="https://img.shields.io/badge/Swing-Desktop_UI-2563EB?style=for-the-badge&amp;logo=openjdk&amp;logoColor=white" alt="Swing — Desktop UI" /></a>
</p>

<p align="center">
  <a href="#project-details">Project details</a> · <a href="#project-setup">Get started</a> · <a href="#project-architecture">How it works</a> · <a href="#creator-developer-maintainer">Creator</a>
</p>

---

<a id="project-details"></a>
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

<a id="project-setup"></a>
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
git clone https://github.com/tracolerd/Kotlin-Scientific-Calculator.git
cd Kotlin-Scientific-Calculator

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

<a id="project-architecture"></a>
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
git clone https://github.com/tracolerd/Kotlin-Scientific-Calculator.git
cd Kotlin-Scientific-Calculator

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
---

---

## Creator, Developer & Maintainer

**Created, designed, developed, documented, and maintained independently by [Nurul Faiyaz](https://github.com/tracolerd).**

<p align="center">
  <a href="https://www.facebook.com/faiyaz.888"><img src="https://img.shields.io/badge/Facebook-1877F2?style=for-the-badge&amp;logo=facebook&amp;logoColor=white" alt="Facebook" /></a>
  <a href="https://www.instagram.com/tracolerd"><img src="https://img.shields.io/badge/Instagram-E4405F?style=for-the-badge&amp;logo=instagram&amp;logoColor=white" alt="Instagram" /></a>
  <a href="https://www.linkedin.com/in/faiyaz-xyz"><img src="https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&amp;logo=linkedin&amp;logoColor=white" alt="LinkedIn" /></a>
  <a href="https://x.com/faiyaz_explores"><img src="https://img.shields.io/badge/X-000000?style=for-the-badge&amp;logo=x&amp;logoColor=white" alt="X" /></a>
  <a href="https://github.com/tracolerd"><img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&amp;logo=github&amp;logoColor=white" alt="GitHub" /></a>
  <a href="mailto:scrollfaiyaz@gmail.com"><img src="https://img.shields.io/badge/Email-D14836?style=for-the-badge&amp;logo=gmail&amp;logoColor=white" alt="Email" /></a>
  <a href="https://nurul-faiyaz.netlify.app/"><img src="https://img.shields.io/badge/Portfolio-Visit%20Website-0F766E?style=for-the-badge&amp;logo=googlechrome&amp;logoColor=white" alt="Website" /></a>
</p>

<p align="center">
  <a href="https://www.facebook.com/faiyaz.888">Facebook</a> ·
  <a href="https://www.instagram.com/tracolerd">Instagram</a> ·
  <a href="https://www.linkedin.com/in/faiyaz-xyz">LinkedIn</a> ·
  <a href="https://x.com/faiyaz_explores">X / Twitter</a> ·
  <a href="https://github.com/tracolerd">GitHub</a> ·
  <a href="mailto:scrollfaiyaz@gmail.com">Email</a> ·
  <a href="https://nurul-faiyaz.netlify.app/">Website</a>
</p>

## License

See [LICENSE](LICENSE) for the license terms.

---

<p align="center"><sub>Portfolio catalog · Project 17 of 22 · <a href="https://github.com/tracolerd">Nurul Faiyaz</a></sub></p>
