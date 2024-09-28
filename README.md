# Prototype of the Enterprise Asset Accounting System and Report Generation. 
 
 <b>Used technologies:</b> Spring Boot, Vaadin 12, Maven, Git, PostgreSQL, Mockito, Lombok  
 
Brief Description 

The enterprise asset accounting system is designed for:

- Storing information about the company's assets
- Analyzing the effectiveness of asset utilization through reports.
   
Assets (in simplified terms) are resources controlled by the company. 
Examples include mobile devices, buildings, cars, etc. 
Assets can be transferred from one company to another.

It is necessary to generate reports to identify:

- The assets that transfer between companies most frequently, i.e., the most in-demand.
- Companies with the highest number of assets.
- The most expensive assets that are also the most in-demand.
- And others.

Client-Side

Companies

- Create a company
- Company list

Assets

- Create an asset
- Asset list
- Generate 100 assets
- Generate 100 transfers of existing random assets from one company to another
 
Reports

- Most in-demand assets
- Companies with the highest number of assets
- Most expensive and most in-demand assets
- And others
 
Elements of the Asset Creation Page

- Asset Name
- Current Company of the Asset
- Date of transfer to the company
- Cost
- Date of Asset Creation (automatically generated)