//
//  AgreementsViewController.h
//  P2PDinner
//
//  Created by Selvam M on 5/31/16.
//  Copyright © 2016 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AgreementsViewController : UIViewController
@property(nonatomic,strong)NSURL *pdfURL;
@property(nonatomic,weak)IBOutlet UIWebView *pdfWebView;
@end
