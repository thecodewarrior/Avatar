/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{vue,html,js,ts}",
  ],
  theme: {
    extend: {
      borderWidth: {
        '1': '1px'
      },
      gridTemplateColumns: {
        'subgrid': 'subgrid',
      },
      gridTemplateRows: {
        'subgrid': 'subgrid',
      },
    }
  },
}

