/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        brand: {
          50:  '#eef7ff',
          100: '#d9edff',
          200: '#bbddff',
          300: '#8bc7ff',
          400: '#54a8ff',
          500: '#2b87fb',
          600: '#1468f0',
          700: '#1153dd',
          800: '#1443b3',
          900: '#163b8d',
          950: '#112558',
        },
        surface: {
          DEFAULT: '#0f1623',
          card:    '#161f2e',
          border:  '#1e2d42',
          muted:   '#253347',
        },
      },
      fontFamily: {
        sans: ['DM Sans', 'system-ui', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace'],
        display: ['Syne', 'sans-serif'],
      },
      borderRadius: {
        xl: '1rem',
        '2xl': '1.25rem',
      },
      boxShadow: {
        card: '0 0 0 1px rgba(30,45,66,0.8), 0 4px 24px rgba(0,0,0,0.4)',
        glow: '0 0 20px rgba(43,135,251,0.25)',
      },
      animation: {
        'fade-in': 'fadeIn 0.3s ease-out',
        'slide-up': 'slideUp 0.3s ease-out',
      },
      keyframes: {
        fadeIn:  { from: { opacity: '0' }, to: { opacity: '1' } },
        slideUp: { from: { opacity: '0', transform: 'translateY(12px)' }, to: { opacity: '1', transform: 'translateY(0)' } },
      },
    },
  },
  plugins: [],
}
