export default {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        surface: '#121127',
        'surface-container': '#1e1d34',
        'surface-container-lowest': '#0c0b21',
        'surface-container-low': '#1a1930',
        'surface-container-high': '#29283f',
        'surface-container-highest': '#33324a',
        'surface-variant': '#33324a',
        'surface-raised': '#1A192E',
        background: '#121127',
        'on-surface': '#e3dffe',
        'on-background': '#e3dffe',
        'on-surface-variant': '#c7c4da',
        'outline-variant': '#464557',
        outline: '#908fa3',
        primary: '#c2c1ff',
        'primary-container': '#310df4',
        'on-primary-container': '#bab8ff',
        'inverse-primary': '#4637ff',
        'border-subtle': 'rgba(236, 232, 240, 0.1)',
        'status-success': '#10B981',
        'status-warning': '#F59E0B',
        'status-error': '#EF4444',
        'status-info': '#3B82F6',
        error: '#ffb4ab'
      },
      borderRadius: {
        DEFAULT: '0.25rem',
        lg: '0.5rem',
        xl: '0.75rem',
        full: '9999px'
      },
      spacing: {
        'container-margin': '32px',
        'card-padding': '24px',
        'section-gap': '48px',
        gutter: '24px',
        'base-unit': '4px'
      },
      fontFamily: {
        sans: ['Hanken Grotesk', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace'],
        'title-md': ['Hanken Grotesk'],
        'body-sm': ['Hanken Grotesk'],
        'label-caps': ['JetBrains Mono'],
        'headline-lg': ['Hanken Grotesk'],
        'body-lg': ['Hanken Grotesk'],
        'data-tabular': ['JetBrains Mono']
      },
      fontSize: {
        'title-md': ['20px', { lineHeight: '28px', fontWeight: '600' }],
        'body-sm': ['14px', { lineHeight: '20px', fontWeight: '400' }],
        'label-caps': ['12px', { lineHeight: '16px', letterSpacing: '0.05em', fontWeight: '500' }],
        'headline-lg': ['32px', { lineHeight: '40px', letterSpacing: '-0.01em', fontWeight: '600' }],
        'body-lg': ['16px', { lineHeight: '24px', fontWeight: '400' }],
        'data-tabular': ['14px', { lineHeight: '20px', fontWeight: '400' }],
        'display-lg': ['48px', { lineHeight: '56px', letterSpacing: '-0.02em', fontWeight: '700' }]
      }
    }
  },
  plugins: []
};
