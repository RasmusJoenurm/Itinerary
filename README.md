
# Itinerary Prettifier

**Itinerary Prettifier** is a Java-based command-line tool developed for **Anywhere Holidays**, a new online travel agency, to 
transform flight itineraries from raw text formats into customer-friendly, readable outputs. This tool processes airport codes, 
dates, and times into a format that customers can easily understand, saving administrators time in reformatting data manually.

## Table of Contents

1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Installation](#installation)
4. [Usage](#usage)
5. [File Structure](#file-structure)
6. [Error Handling](#error-handling)
7. [Examples](#examples)

## Project Overview

When customers purchase a flight, administrators use an online portal to generate a text-based itinerary. The raw output, 
however, is formatted in a way that’s difficult for customers to interpret, as it includes airport codes, technical date 
formats, and inconsistent line spacing. **Itinerary Prettifier** addresses these issues by transforming the text file into 
a user-friendly format, converting airport codes to names, formatting dates/times, and removing unnecessary blank lines.

## Features

- **Airport Code Transformation**: Converts IATA and ICAO codes into airport or city names using a provided CSV lookup.
  - **Airport Name**: Codes prefixed with `#` (IATA, e.g., `#LAX`) or `##` (ICAO, e.g., `##EGLL`) are replaced with the full airport name, such as “Los Angeles International Airport.”
  - **City Name**: Codes prefixed with `*#` or `*##` will return the city name associated with the airport, such as “London” for `*##EGLL`.
- **Date and Time Formatting**: Transforms ISO 8601 date and time formats into readable formats.
- **Whitespace Management**: Limits consecutive blank lines and removes unnecessary line-break characters.
- **Robust Error Handling**: Handles missing files, malformed data, and invalid input parameters gracefully.
- **Dynamic Lookup Handling**: Allows for flexible airport lookup CSV structure with random column order.
- **Formatted Console Output**: Uses colored and formatted messages for statuses and errors.

## Installation

To install and run **Itinerary Prettifier**, follow these steps:

**Clone the repository**:
   ```bash
   git clone https://gitea.kood.tech/rasmusjoenurm/Itinerary.git
   cd src
   ```

## Usage

Run the tool from the command line with the following format:

```bash
java Prettifier <inputFile> <outputFile> <airportLookupFile>
```

### Command-Line Arguments

- `<inputFile>`: Path to the raw itinerary text file.
- `<outputFile>`: Path where the prettified itinerary will be saved.
- `<airportLookupFile>`: Path to the CSV file containing airport codes and names.

### Example

```bash
java Prettifier.java ../input.txt ../output.txt ../airports_lookup.csv
```

### Flags

- `-h`: Displays usage information.
  ```bash
  java Prettifier -h
  ```

## File Structure

```
├── Prettifier.java            # Main entry point for the command-line tool
├── ItineraryProcessor.java     # Core processing logic for itinerary transformation
├── FileHandler.java            # Handles file reading and writing operations
├── ErrorHandler.java           # Manages error messages and handling
├── DateTimeFormatterHelper.java # Date and time formatting helper methods
├── TerminalFormatter.java      # Console output formatting for statuses, errors, and success messages
├── README.md                   # Project documentation
└── airports_lookup.csv         # Sample lookup file with airport codes and details
```

## Error Handling

**Itinerary Prettifier** includes comprehensive error handling to guide users if issues arise:

- **Invalid Argument Count**: Displays usage instructions.
- **Input File Not Found**: Alerts if the specified input file is missing.
- **Airport Lookup File Not Found**: Alerts if the airport lookup file is missing.
- **Malformed Airport Data**: Detects missing or malformed data in the lookup CSV and halts processing.
- **Malformed Dates or Times**: Leaves date and time entries unchanged if they don't follow the correct ISO format.

### Error Example

If `airports_lookup.csv` is missing a column:

```
Error: Airport lookup data is malformed.
```

### Input Example

Given an `input.txt` file:

```
Flight *#LAX to *#JFK on D(2023-11-15T14:30Z) departs at T24(2023-11-15T14:30Z)
Arrival at *##EGLL at T12(2023-11-16T07:45+01:00)
```

With `airports_lookup.csv` entries for `LAX`, `JFK`, and `EGLL`.

### Output Example

The tool generates `output.txt`:

```
Flight Los Angeles to New York City on 15 Nov 2023 departs at 14:30 (+00:00)
Arrival at London at 07:45AM (+01:00)
```
